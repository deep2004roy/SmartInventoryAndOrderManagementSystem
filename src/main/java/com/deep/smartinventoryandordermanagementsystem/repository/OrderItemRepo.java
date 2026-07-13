package com.deep.smartinventoryandordermanagementsystem.repository;

import com.deep.smartinventoryandordermanagementsystem.model.Order;
import com.deep.smartinventoryandordermanagementsystem.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrder(Order order);
    @Query("""
       SELECT oi.product.name, SUM(oi.quantity)
       FROM OrderItem oi
       GROUP BY oi.product.name
       ORDER BY SUM(oi.quantity) DESC
       """)
    List<Object[]> getTopSellingProducts();

    @Query("""
       SELECT COALESCE(SUM(oi.quantity), 0)
       FROM OrderItem oi
       JOIN oi.order o
       WHERE o.status = 'DELIVERED'
       """)
    Long getTotalItemsSold();

    @Query("""
       SELECT COALESCE(
           SUM(
               (oi.price - oi.costPrice) * oi.quantity
           ),
           0
       )
       FROM OrderItem oi
       JOIN oi.order o
       WHERE o.status = 'DELIVERED'
       """)
    BigDecimal getTotalProfit();

    @Query("""
SELECT oi.productName,
       SUM(oi.quantity)
FROM OrderItem oi
JOIN oi.order o
WHERE o.status='DELIVERED'
GROUP BY oi.productName
ORDER BY SUM(oi.quantity) ASC
""")
    List<Object[]> getLeastSellingProducts();
}
