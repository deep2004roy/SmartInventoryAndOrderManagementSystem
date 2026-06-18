package com.deep.smartinventoryandordermanagementsystem.repository;

import com.deep.smartinventoryandordermanagementsystem.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    boolean existsBySku(String sku);

    boolean existsByBarcode(String barcode);
    Page<Product> findByNameContaining(String search, Pageable pageable);


    Page<Product> findByCategoryContaining(String category, Pageable pageable);
    @Query("SELECT SUM(p.quantity) FROM Product p")
    Long getTotalStock();


    List<Product> findByQuantityLessThanEqual(Integer quantity);
}
