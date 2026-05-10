package com.deep.smartinventoryandordermanagementsystem.controller;

import com.deep.smartinventoryandordermanagementsystem.model.Product;
import com.deep.smartinventoryandordermanagementsystem.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    public Product addProduct(@RequestBody Product product){
        return productService.addProduct(product);
    }

    @GetMapping("/products")
    public List<Product> getProducts(){
        return productService.getProducts();
    }

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable int id){
        return productService.getProduct(id);
    }

    @PutMapping("/products")
    public Product updateProduct(@RequestBody Product product){
        return productService.updateProduct(product);
    }

    @DeleteMapping("/products/{id}")
    public Product deleteProduct(@PathVariable int id){
        Product product1 = getProduct(id);
        productService.deleteProduct(id);
        return product1;
    }
}
