package com.deep.smartinventoryandordermanagementsystem.repository;

import com.deep.smartinventoryandordermanagementsystem.model.Order;
import com.deep.smartinventoryandordermanagementsystem.model.OrderStatus;
import com.deep.smartinventoryandordermanagementsystem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
    @Query("SELECT SUM(o.totalAmount) FROM Order o")
    BigDecimal getTotalRevenue();
    List<Order> findTop5ByOrderByCreatedAtDesc();
    long countByStatus(OrderStatus status);
    @Query("""
       SELECT YEAR(o.createdAt),
              MONTH(o.createdAt),
              SUM(o.totalAmount)
       FROM Order o
       GROUP BY YEAR(o.createdAt), MONTH(o.createdAt)
       ORDER BY YEAR(o.createdAt), MONTH(o.createdAt)
       """)
    List<Object[]> getMonthlyRevenue();
List<Order> findByUser(User user);
}
