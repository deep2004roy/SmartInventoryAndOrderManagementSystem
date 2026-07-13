package com.deep.smartinventoryandordermanagementsystem.service;

import com.deep.smartinventoryandordermanagementsystem.dto.analytics.ProfitSummaryDTO;
import com.deep.smartinventoryandordermanagementsystem.repository.OrderItemRepo;
import com.deep.smartinventoryandordermanagementsystem.repository.OrderRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ProfitAnalyticsService {
    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;

    public ProfitAnalyticsService(
            OrderRepo orderRepo,
            OrderItemRepo orderItemRepo) {

        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
    }
    public ProfitSummaryDTO getProfitSummary() {

        BigDecimal revenue =
                orderRepo.getTotalRevenue();

        BigDecimal profit =
                orderItemRepo.getTotalProfit();

        ProfitSummaryDTO dto =
                new ProfitSummaryDTO();

        dto.setRevenue(revenue);
        dto.setProfit(profit);

        if (revenue.compareTo(BigDecimal.ZERO) > 0) {

            dto.setMargin(
                    profit.multiply(
                            BigDecimal.valueOf(100)
                    ).divide(
                            revenue,
                            2,
                            RoundingMode.HALF_UP
                    )
            );
        } else {

            dto.setMargin(BigDecimal.ZERO);
        }

        return dto;
    }
}
