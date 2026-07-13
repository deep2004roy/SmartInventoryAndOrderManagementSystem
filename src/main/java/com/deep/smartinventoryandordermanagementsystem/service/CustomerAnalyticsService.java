package com.deep.smartinventoryandordermanagementsystem.service;

import com.deep.smartinventoryandordermanagementsystem.dto.analytics.CustomerAnalyticsSummaryDTO;
import com.deep.smartinventoryandordermanagementsystem.dto.analytics.TopCustomerDTO;
import com.deep.smartinventoryandordermanagementsystem.repository.OrderRepo;
import com.deep.smartinventoryandordermanagementsystem.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CustomerAnalyticsService {
    private final UserRepo userRepo;
    private final OrderRepo orderRepo;

    public CustomerAnalyticsService(
            UserRepo userRepo,
            OrderRepo orderRepo) {

        this.userRepo = userRepo;
        this.orderRepo = orderRepo;
    }

    public CustomerAnalyticsSummaryDTO getSummary() {

        CustomerAnalyticsSummaryDTO dto =
                new CustomerAnalyticsSummaryDTO();

        dto.setTotalCustomers(
                userRepo.count()
        );

        dto.setCustomersWithOrders(
                orderRepo.countCustomersWithOrders()
        );

        dto.setRepeatCustomers(
                orderRepo.countRepeatCustomers()
        );

        return dto;
    }

    public List<TopCustomerDTO> getTopCustomers() {

        return orderRepo.getTopCustomers()
                .stream()
                .limit(10)
                .map(row ->
                        new TopCustomerDTO(
                                (String) row[0],
                                ((Number) row[1]).longValue(),
                                (BigDecimal) row[2]
                        )
                )
                .toList();
    }
}
