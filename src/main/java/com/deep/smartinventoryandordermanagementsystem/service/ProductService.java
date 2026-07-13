package com.deep.smartinventoryandordermanagementsystem.service;

import com.deep.smartinventoryandordermanagementsystem.dto.product.ProductRequest;
import com.deep.smartinventoryandordermanagementsystem.dto.product.ProductResponseDTO;
import com.deep.smartinventoryandordermanagementsystem.exception.DuplicateBarcodeException;
import com.deep.smartinventoryandordermanagementsystem.exception.DuplicateSkuException;
import com.deep.smartinventoryandordermanagementsystem.exception.InvalidSortParameterException;
import com.deep.smartinventoryandordermanagementsystem.exception.ProductNotFoundException;
import com.deep.smartinventoryandordermanagementsystem.model.Product;
import com.deep.smartinventoryandordermanagementsystem.model.StockMovementType;
import com.deep.smartinventoryandordermanagementsystem.repository.ProductRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepo productRepo;
    private final StockMovementService stockMovementService;

    public ProductService(ProductRepo productRepo, StockMovementService stockMovementService) {
        this.productRepo = productRepo;
        this.stockMovementService = stockMovementService;
    }

    private ProductResponseDTO mapToDto(Product product) {

        ProductResponseDTO dto =
                new ProductResponseDTO();

        dto.setProductId(product.getProductId());
        dto.setSku(product.getSku());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setBrand(product.getBrand());
        dto.setCategory(product.getCategory());
        dto.setCostPrice(product.getCostPrice());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setReorderLevel(product.getReorderLevel());
        dto.setActive(product.getActive());
        dto.setBarcode(product.getBarcode());
        dto.setUnit(product.getUnit());
        dto.setImageUrl(product.getImageUrl());

        return dto;
    }

    public ProductResponseDTO  addProduct(ProductRequest request,
                              MultipartFile image) {
        if(productRepo.existsBySku(request.getSku())){
            throw new DuplicateSkuException(
                    "SKU already exists"
            );
        }


        if(productRepo.existsByBarcode(
                request.getBarcode())){

            throw new DuplicateBarcodeException(
                    "Barcode already exists"
            );
        }

        Product product = new Product();

        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setBrand(request.getBrand());
        product.setCostPrice(request.getCostPrice());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setCategory(request.getCategory());
        product.setReorderLevel(request.getReorderLevel());
        product.setActive(request.getActive());
        product.setBarcode(request.getBarcode());
        product.setUnit(request.getUnit());

        try{
            String uploadDir = "uploads/";
            if(image != null && !image.isEmpty()){
                String fileName = image.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);

                if(!Files.exists(uploadPath)){
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);

                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                product.setImageUrl(fileName);
            }else {
                product.setImageUrl("default-product.webp");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
        Product savedProduct = productRepo.save(product);

        stockMovementService.recordMovement(
                savedProduct,
                savedProduct.getQuantity(),
                StockMovementType.PURCHASE,
                "INITIAL-STOCK"
        );

        return mapToDto(savedProduct);
    }


    public ProductResponseDTO getProductById(Long id) {

        Product product = productRepo.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found " + id
                        ));

        return mapToDto(product);
    }

    public ProductResponseDTO  updateProduct(Long id, String name,
                                 String description, BigDecimal price,
                                 Integer quantity, String category,
                                 Boolean active,
                                 MultipartFile image) {

        Product product = productRepo.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found " + id
                        ));

        int oldQuantity = product.getQuantity();

        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);
        product.setActive(active);


        product.setQuantity(quantity);

        try {
            String uploadDir = "uploads/";
            if (image != null && !image.isEmpty()) {
                String fileName = image.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(image.getInputStream(),
                        filePath,
                        StandardCopyOption.REPLACE_EXISTING);

                product.setImageUrl(fileName);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }

        Product savedProduct = productRepo.save(product);

        int diff = quantity - oldQuantity;

        if (diff != 0) {
            stockMovementService.recordMovement(
                    savedProduct,
                    diff,
                    StockMovementType.ADJUSTMENT,
                    "ADMIN-UPDATE"
            );
        }

        return mapToDto(savedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepo.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found " + id));
        product.setActive(false);
        productRepo.save(product);
    }

    private static final List<String> ALLOWED_SORT_FIELDS = List.of(
            "name",
            "price",
            "quantity",
            "category",
            "brand",
            "createdAt"
    );

    private Sort buildSort(String sort) {

        if (sort == null || sort.isBlank()) {
            return Sort.unsorted();
        }

        String[] values = sort.split(",");

        if (values.length != 2) {
            throw new InvalidSortParameterException(
                    "Sort format should be field,direction"
            );
        }

        String field = values[0].trim();
        String direction = values[1].trim();

        if (!ALLOWED_SORT_FIELDS.contains(field)) {
            throw new InvalidSortParameterException(
                    "Invalid sort field: " + field
            );
        }

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {

            throw new InvalidSortParameterException(
                    "Sort direction must be asc or desc"
            );
        }

        return direction.equalsIgnoreCase("asc")
                ? Sort.by(field).ascending()
                : Sort.by(field).descending();
    }


    public Page<ProductResponseDTO> getProducts(
            int page,
            int size,
            String search,
            String category,
            Boolean active,
            String sort
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        buildSort(sort)
                );

        return productRepo
                .findProducts(
                        search,
                        category,
                        active,
                        pageable
                )
                .map(this::mapToDto);
    }
}
