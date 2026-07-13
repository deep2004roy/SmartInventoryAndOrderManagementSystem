package com.deep.smartinventoryandordermanagementsystem.repository;

import com.deep.smartinventoryandordermanagementsystem.model.Order;
import com.deep.smartinventoryandordermanagementsystem.model.OrderStatus;
import com.deep.smartinventoryandordermanagementsystem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
    @Query("""
           SELECT COALESCE(SUM(o.totalAmount), 0)
           FROM Order o
           WHERE o.status = 'DELIVERED'
           """)
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


    @Query("""
           SELECT COALESCE(AVG(o.totalAmount), 0)
           FROM Order o
           WHERE o.status = 'DELIVERED'
           """)
    BigDecimal getAverageOrderValue();

    @Query("""
       SELECT COUNT(DISTINCT o.user.id)
       FROM Order o
       """)
    Long countCustomersWithOrders();

    @Query(value = """
        SELECT COUNT(*)
        FROM (
            SELECT user_id
            FROM orders
            GROUP BY user_id
            HAVING COUNT(id) > 1
        ) t
        """,
            nativeQuery = true)
    Long countRepeatCustomers();

    @Query("""
       SELECT o.customerName,
              COUNT(o.id),
              SUM(o.totalAmount)
       FROM Order o
       WHERE o.status = 'DELIVERED'
       GROUP BY o.customerName
       ORDER BY SUM(o.totalAmount) DESC
       """)
    List<Object[]> getTopCustomers();

    @Query("""
       SELECT o
       FROM Order o
       WHERE (:search IS NULL OR
              LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :search, '%'))
              OR LOWER(o.customerName) LIKE LOWER(CONCAT('%', :search, '%')))
       AND (:status IS NULL OR o.status = :status)
       """)
    Page<Order> findOrders(
            String search,
            OrderStatus status,
            Pageable pageable
    );

    Page<Order> findByUser(
            User user,
            Pageable pageable
    );

    Page<Order> findByUserAndStatus(
            User user,
            OrderStatus status,
            Pageable pageable
    );

}
