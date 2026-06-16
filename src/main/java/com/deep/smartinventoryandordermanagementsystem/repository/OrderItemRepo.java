package com.deep.smartinventoryandordermanagementsystem.repository;

import com.deep.smartinventoryandordermanagementsystem.model.Order;
import com.deep.smartinventoryandordermanagementsystem.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}
