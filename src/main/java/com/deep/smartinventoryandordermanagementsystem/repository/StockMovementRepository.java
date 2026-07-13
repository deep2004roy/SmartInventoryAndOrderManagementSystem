package com.deep.smartinventoryandordermanagementsystem.repository;

import com.deep.smartinventoryandordermanagementsystem.model.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    List<StockMovement> findByProductProductIdOrderByCreatedAtDesc(Long productId);
    @Query("""
       SELECT COALESCE(SUM(sm.quantityChanged),0)
       FROM StockMovement sm
       WHERE sm.type = 'PURCHASE'
       """)
    Long getTotalPurchased();

    @Query("""
       SELECT COALESCE(SUM(ABS(sm.quantityChanged)),0)
       FROM StockMovement sm
       WHERE sm.type = 'ORDER'
       """)
    Long getTotalSold();

    @Query("""
       SELECT COALESCE(SUM(sm.quantityChanged),0)
       FROM StockMovement sm
       WHERE sm.type = 'RETURN'
       """)
    Long getTotalReturned();

    @Query("""
       SELECT COALESCE(SUM(ABS(sm.quantityChanged)),0)
       FROM StockMovement sm
       WHERE sm.type = 'ADJUSTMENT'
       """)
    Long getTotalAdjusted();
}
