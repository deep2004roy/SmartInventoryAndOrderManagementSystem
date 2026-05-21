package com.deep.smartinventoryandordermanagementsystem.service;

import com.deep.smartinventoryandordermanagementsystem.exception.ProductNotFoundException;
import com.deep.smartinventoryandordermanagementsystem.model.Product;
import com.deep.smartinventoryandordermanagementsystem.repository.ProductRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public Product addProduct(Product product) {
            return productRepo.save(product);
    }

    public List<Product> getProducts() {
        return productRepo.findAll().stream().filter(product ->
                product.isActive()).toList();
    }

    public Product getProductById(int id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
//        return productRepo.getReferenceById(id);
    }

    public Product updateProduct(int id, Product product) {
        Product product1 = getProductById(id);
        product1.setName(product.getName());
        product1.setCategory(product.getCategory());
        product1.setDescription(product.getDescription());
        product1.setPrice(product.getPrice());
        product1.setQuantity(product.getQuantity());
        product1.setActive(product.isActive());
        return productRepo.save(product1);
    }

    public void deleteProduct(int id) {
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
