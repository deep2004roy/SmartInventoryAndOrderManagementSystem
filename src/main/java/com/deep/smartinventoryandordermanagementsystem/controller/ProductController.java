package com.deep.smartinventoryandordermanagementsystem.controller;

import com.deep.smartinventoryandordermanagementsystem.model.Product;
import com.deep.smartinventoryandordermanagementsystem.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    public Product addProduct(@RequestBody Product product){
        return productService.addProduct(product);
    }

//    @GetMapping("/products")
//    public List<Product> getProducts(@RequestParam(required = false) String search,
//                                     @RequestParam(required = false) String category,
//                                     @RequestParam(required = false) String sort){
//        if(search != null && !search.isEmpty()){
//            return productService.searchProducts(search);
//        }
//
//        if(category != null && !category.isEmpty()){
//            return productService.filterProducts(category);
//        }
//
//        if(sort != null && !sort.isEmpty()){
//            return productService.sortedByPrice(sort);
//        }
//
//        return productService.getProducts();
//    }

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
    public Product getProductById(@PathVariable int id){
        return productService.getProductById(id);
    }

    @PutMapping("/products/{id}")
    public Product updateProductById(@PathVariable int id, @RequestBody Product product){
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/products/{id}")
    public Product deleteProduct(@PathVariable int id){
        Product product1 = getProductById(id);
        productService.deleteProduct(id);
        return product1;
    }


}
