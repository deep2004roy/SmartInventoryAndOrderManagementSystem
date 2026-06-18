package com.deep.smartinventoryandordermanagementsystem.service;

import com.deep.smartinventoryandordermanagementsystem.dto.product.ProductRequest;
import com.deep.smartinventoryandordermanagementsystem.exception.DuplicateBarcodeException;
import com.deep.smartinventoryandordermanagementsystem.exception.DuplicateSkuException;
import com.deep.smartinventoryandordermanagementsystem.exception.ProductNotFoundException;
import com.deep.smartinventoryandordermanagementsystem.model.Product;
import com.deep.smartinventoryandordermanagementsystem.repository.ProductRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public Product addProduct(ProductRequest request,
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
            return productRepo.save(product);
    }

    public List<Product> getProducts() {
        return productRepo.findAll().stream().filter(product ->
                Boolean.TRUE.equals(product.getActive())).toList();
    }

    public Product getProductById(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    public Product updateProduct(Long id, String name,
                                 String description, Double price,
                                 Integer quantity, String category,
                                 Boolean active,
                                 MultipartFile image) {
        Product product = getProductById(id);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setCategory(category);
        product.setActive(active);
        try{
            String uploadDir = "uploads/";
            if(image != null && !image.isEmpty()){
                String fileName = image.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);
                if(!Files.exists(uploadPath)){
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(image.getInputStream(),
                        filePath,
                        StandardCopyOption.REPLACE_EXISTING);
                product.setImageUrl(fileName);
            }
        }catch (IOException e){
            throw new RuntimeException("Failed to upload image", e);
        }

        return productRepo.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        product.setActive(false);
        productRepo.save(product);
    }

    public Page<Product> searchProducts(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepo.findByNameContaining(search, pageable);
    }

    public Page<Product> filterProducts(int page, int size, String category){
        Pageable pageable = PageRequest.of(page, size);
        return productRepo.findByCategoryContaining(category, pageable);
    }

    public Page<Product> sortedByPrice(int page, int size, String sort) {
        String[] values = sort.split(",");
        String field = values[0];
        String direction = values[1];

        Sort sortObj = direction.equals("asc")?Sort.by(field).ascending(): Sort.by(field).descending();
        Pageable pageable = PageRequest.of(page, size, sortObj);

        return productRepo.findAll(pageable);

    }

    public Page<Product> getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepo.findAll(pageable);
    }
}
