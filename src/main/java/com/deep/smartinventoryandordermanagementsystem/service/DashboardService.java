package com.deep.smartinventoryandordermanagementsystem.service;

import com.deep.smartinventoryandordermanagementsystem.dto.*;
import com.deep.smartinventoryandordermanagementsystem.dto.dashboard.*;
import com.deep.smartinventoryandordermanagementsystem.model.Order;
import com.deep.smartinventoryandordermanagementsystem.model.OrderStatus;
import com.deep.smartinventoryandordermanagementsystem.model.Product;
import com.deep.smartinventoryandordermanagementsystem.repository.OrderItemRepo;
import com.deep.smartinventoryandordermanagementsystem.repository.OrderRepo;
import com.deep.smartinventoryandordermanagementsystem.repository.ProductRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class DashboardService {
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;

    public DashboardService(ProductRepo productRepo, OrderRepo orderRepo, OrderItemRepo orderItemRepo) {
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
    }

    private InventorySummaryDTO buildInventorySummary(){
        InventorySummaryDTO inventory = new InventorySummaryDTO();
        inventory.setTotalProducts(productRepo.count());

        Long totalStock = productRepo.getTotalStock();

        inventory.setTotalStock(
                totalStock == null ? 0 : totalStock
        );

        inventory.setLowStockProducts(
                productRepo.countLowStockProducts()
        );

        inventory.setOutOfStockProducts(
                productRepo.countByQuantity(0)
        );

        BigDecimal inventoryCostValue =
                productRepo.getInventoryCostValue();

        if (inventoryCostValue == null) {
            inventoryCostValue = BigDecimal.ZERO;
        }

        BigDecimal inventorySellingValue =
                productRepo.getInventorySellingValue();

        if (inventorySellingValue == null) {
            inventorySellingValue = BigDecimal.ZERO;
        }

        inventory.setInventoryCostValue(
                inventoryCostValue
        );

        inventory.setInventorySellingValue(
                inventorySellingValue
        );

        inventory.setPotentialProfit(
                inventorySellingValue.subtract(
                        inventoryCostValue
                )
        );

        return inventory;
    }

    private SalesSummaryDTO buildSalesSummary(){
        SalesSummaryDTO sales = new SalesSummaryDTO();
        sales.setRevenue(
                Optional.ofNullable(
                        orderRepo.getTotalRevenue()
                ).orElse(BigDecimal.ZERO)
        );

        sales.setTotalItemsSold(
                Optional.ofNullable(
                        orderItemRepo.getTotalItemsSold()
                ).orElse(0L)
        );

        sales.setAverageOrderValue(
                Optional.ofNullable(
                        orderRepo.getAverageOrderValue()
                ).orElse(BigDecimal.ZERO)
        );

        sales.setTotalProfit(
                Optional.ofNullable(
                        orderItemRepo.getTotalProfit()
                ).orElse(BigDecimal.ZERO)
        );
        return sales;
    }

    private OrderSummaryStatsDTO buildOrderSummary(){
        OrderSummaryStatsDTO orders = new OrderSummaryStatsDTO();



        orders.setTotalOrders(orderRepo.count());

        orders.setPendingOrders(
                orderRepo.countByStatus(
                        OrderStatus.PENDING
                )
        );

        orders.setConfirmedOrders(
                orderRepo.countByStatus(
                        OrderStatus.CONFIRMED
                )
        );

        orders.setShippedOrders(
                orderRepo.countByStatus(
                        OrderStatus.SHIPPED
                )
        );

        orders.setDeliveredOrders(
                orderRepo.countByStatus(
                        OrderStatus.DELIVERED
                )
        );

        orders.setCancelledOrders(
                orderRepo.countByStatus(
                        OrderStatus.CANCELLED
                )
        );

        return orders;
    }


    private List<RecentOrderDTO> getRecentOrders(){
        List<Order> orders = orderRepo.findTop5ByOrderByCreatedAtDesc();
        List<RecentOrderDTO> recentOrders = orders.stream().map(order -> {
            RecentOrderDTO dto = new RecentOrderDTO();
            dto.setId(order.getId());
            dto.setCreatedAt(order.getCreatedAt());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setStatus(order.getStatus());
            return dto;
        }).toList();
        return recentOrders;
    }

    public List<MonthlyRevenueDTO> getMonthlyRevenue(){
        List<Object[]> results = orderRepo.getMonthlyRevenue();
        List<MonthlyRevenueDTO> monthlyRevenue = new ArrayList<>();
        for(Object[] row : results){
            Integer monthNumber = (Integer) row[0];
            BigDecimal rev = (BigDecimal) row[1];
            String month = Month.of(monthNumber).getDisplayName(TextStyle.SHORT,
                    Locale.ENGLISH);
            monthlyRevenue.add(new MonthlyRevenueDTO(month, rev));
        }
        return monthlyRevenue;
    }

    private List<TopSellingProductDTO> getTopSellingProducts(){
        List<Object[]> result = orderItemRepo.getTopSellingProducts();
        List<TopSellingProductDTO> topSellingProducts =
                result.stream().limit(5)
                        .map(row -> new TopSellingProductDTO((String) row[0],
                                ((Number) row[1]).longValue())).toList();
        return  topSellingProducts;
    }


   public DashboardSummaryDTO getSummary() {
       DashboardSummaryDTO summary = new DashboardSummaryDTO();

       summary.setInventorySummary(buildInventorySummary());
       summary.setSalesSummary(buildSalesSummary());
       summary.setOrderSummary(buildOrderSummary());

       summary.setRecentOrders(getRecentOrders());

        summary.setMonthlyRevenue(getMonthlyRevenue());
        summary.setTopSellingProducts(getTopSellingProducts());
        return summary;
    }
}
