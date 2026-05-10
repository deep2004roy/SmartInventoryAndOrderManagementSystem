package com.deep.smartinventoryandordermanagementsystem.service;

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
        if(product.getId() != 0)
            return productRepo.save(product);
        return new Product();
    }

    public List<Product> getProducts() {
        return productRepo.findAll();
    }

    public Product getProduct(int id) {
        return productRepo.findById(id).orElse(new Product());
    }

    public Product updateProduct(Product product) {
        Product product1 = productRepo.findById(product.getId()).orElseThrow();
        if(product1.getId() == product.getId()){
            return productRepo.save(product);
        }else{
            return new Product();
        }
    }

    public void deleteProduct(int id) {
        productRepo.deleteById(id);
    }
}
