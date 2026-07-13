package com.deep.smartinventoryandordermanagementsystem.service;

import com.deep.smartinventoryandordermanagementsystem.dto.dashboard.SalesSummaryDTO;
import com.deep.smartinventoryandordermanagementsystem.repository.OrderItemRepo;
import com.deep.smartinventoryandordermanagementsystem.repository.OrderRepo;
import org.springframework.stereotype.Service;

@Service
public class SalesAnalyticsService {
    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;

    public SalesAnalyticsService(
            OrderRepo orderRepo,
            OrderItemRepo orderItemRepo) {

        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
    }

    public SalesSummaryDTO getSalesSummary() {

        SalesSummaryDTO dto =
                new SalesSummaryDTO();

        dto.setRevenue(orderRepo.getTotalRevenue());

        dto.setAverageOrderValue(
                orderRepo.getAverageOrderValue()
        );

        dto.setTotalItemsSold(
                orderItemRepo.getTotalItemsSold()
        );

        dto.setTotalProfit(
                orderItemRepo.getTotalProfit()
        );

        return dto;
    }
}
