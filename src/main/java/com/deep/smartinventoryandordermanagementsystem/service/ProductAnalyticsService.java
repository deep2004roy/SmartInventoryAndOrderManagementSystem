package com.deep.smartinventoryandordermanagementsystem.service;

import com.deep.smartinventoryandordermanagementsystem.dto.analytics.ProductPerformanceDTO;
import com.deep.smartinventoryandordermanagementsystem.repository.OrderItemRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductAnalyticsService {
    private final OrderItemRepo orderItemRepo;

    public ProductAnalyticsService(
            OrderItemRepo orderItemRepo) {

        this.orderItemRepo = orderItemRepo;
    }
    public List<ProductPerformanceDTO>
    getTopSellingProducts() {

        return orderItemRepo.getTopSellingProducts()
                .stream()
                .limit(10)
                .map(row ->
                        new ProductPerformanceDTO(
                                (String) row[0],
                                ((Number) row[1]).longValue()
                        )
                ).toList();
    }
    public List<ProductPerformanceDTO>
    getLeastSellingProducts() {

        return orderItemRepo.getLeastSellingProducts()
                .stream()
                .limit(10)
                .map(row ->
                        new ProductPerformanceDTO(
                                (String) row[0],
                                ((Number) row[1]).longValue()
                        )
                ).toList();
    }

}
