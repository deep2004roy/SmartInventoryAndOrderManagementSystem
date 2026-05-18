package com.deep.smartinventoryandordermanagementsystem.service;

import com.deep.smartinventoryandordermanagementsystem.exception.ProductNotFoundException;
import com.deep.smartinventoryandordermanagementsystem.model.Product;
import com.deep.smartinventoryandordermanagementsystem.repository.ProductRepo;
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

    public List<Product> searchProducts(String keyword) {
        return productRepo.findByNameContaining(keyword);
    }
}
