package com.deep.smartinventoryandordermanagementsystem.repository;

import com.deep.smartinventoryandordermanagementsystem.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    boolean existsBySku(String sku);

    boolean existsByBarcode(String barcode);
    @Query("""
    SELECT p
    FROM Product p
    WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(p.barcode) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<Product> searchProducts(
            @Param("keyword") String keyword,
            Pageable pageable
    );

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

    @Query("""
SELECT p
FROM Product p
WHERE (:search IS NULL OR
       LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
       LOWER(p.sku) LIKE LOWER(CONCAT('%', :search, '%')) OR
       LOWER(p.barcode) LIKE LOWER(CONCAT('%', :search, '%')) OR
       LOWER(p.brand) LIKE LOWER(CONCAT('%', :search, '%')))
AND (:category IS NULL OR
     LOWER(p.category) = LOWER(:category))
AND (:active IS NULL OR
     p.active = :active)
""")
    Page<Product> findProducts(
            String search,
            String category,
            Boolean active,
            Pageable pageable);

    @Query("""
       SELECT p
       FROM Product p
       WHERE p.quantity > 0
       AND p.quantity <= p.reorderLevel
       AND p.active = true
       """)
    List<Product> findLowStockProducts();
}
