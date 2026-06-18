package com.deep.smartinventoryandordermanagementsystem.controller;

import com.deep.smartinventoryandordermanagementsystem.dto.product.ProductRequest;
import com.deep.smartinventoryandordermanagementsystem.model.Product;
import com.deep.smartinventoryandordermanagementsystem.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    public Product addProduct(@Valid @ModelAttribute ProductRequest request,
                              @RequestParam(required = false)
                              MultipartFile image){
        return productService.addProduct(request, image);
    }

    @GetMapping("/products")
    public Page<Product> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,

            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String sort
    ){

        if(search != null && !search.isEmpty()){
            return productService.searchProducts(page, size, search);
        }

        if(category != null && !category.isEmpty()){
            return productService.filterProducts(page, size, category);
        }

        if(sort != null && !sort.isEmpty()){
            return productService.sortedByPrice(page, size, sort);
        }
        return  productService.getProducts(page, size);
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @PutMapping("/products/{id}")
    public Product updateProductById(@PathVariable Long id,
                                     @RequestParam String name,
                                     @RequestParam String description,
                                     @RequestParam Double price,
                                     @RequestParam Integer quantity,
                                     @RequestParam String category,
                                     @RequestParam Boolean active,
                                     @RequestParam(required = false) MultipartFile image){
        return productService.updateProduct(id, name,
                description, price, quantity, category,
                active, image);
    }

    @DeleteMapping("/products/{id}")
    public Product deleteProduct(@PathVariable Long id){
        Product product1 = getProductById(id);
        productService.deleteProduct(id);
        return product1;
    }


}
