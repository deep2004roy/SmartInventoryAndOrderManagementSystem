package com.deep.smartinventoryandordermanagementsystem.repository;

import com.deep.smartinventoryandordermanagementsystem.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    boolean existsBySku(String sku);

    boolean existsByBarcode(String barcode);
    Page<Product> findByNameContaining(String search, Pageable pageable);


    Page<Product> findByCategoryContaining(String category, Pageable pageable);
    @Query("SELECT SUM(p.quantity) FROM Product p")
    Long getTotalStock();

    @Query("""
       SELECT COALESCE(SUM(p.costPrice * p.quantity), 0)
       FROM Product p
       """)
    BigDecimal getInventoryCostValue();

    @Query("""
       SELECT COALESCE(SUM(p.price * p.quantity), 0)
       FROM Product p
       """)
    BigDecimal getInventorySellingValue();

    @Query("""
       SELECT p
       FROM Product p
       WHERE p.quantity <= p.reorderLevel
       """)
    List<Product> findProductsBelowReorderLevel();


    @Query("""
       SELECT p
       FROM Product p
       WHERE p.quantity = 0
       """)
    List<Product> findOutOfStockProducts();

    @Query("""
       SELECT COUNT(p)
       FROM Product p
       WHERE p.quantity <= p.reorderLevel
       """)
    long countLowStockProducts();

    long countByQuantity(Integer quantity);
}
