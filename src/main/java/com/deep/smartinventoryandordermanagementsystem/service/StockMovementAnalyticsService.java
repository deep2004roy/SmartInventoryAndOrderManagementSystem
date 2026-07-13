package com.deep.smartinventoryandordermanagementsystem.service;

import com.deep.smartinventoryandordermanagementsystem.dto.analytics.StockMovementSummaryDTO;
import com.deep.smartinventoryandordermanagementsystem.model.StockMovement;
import com.deep.smartinventoryandordermanagementsystem.repository.StockMovementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockMovementAnalyticsService {
    private final StockMovementRepository repo;

    public StockMovementAnalyticsService(
            StockMovementRepository repo) {

        this.repo = repo;
    }

    public StockMovementSummaryDTO getSummary() {

        StockMovementSummaryDTO dto =
                new StockMovementSummaryDTO();

        dto.setPurchases(
                repo.getTotalPurchased()
        );

        dto.setSales(
                repo.getTotalSold()
        );

        dto.setReturns(
                repo.getTotalReturned()
        );

        dto.setAdjustments(
                repo.getTotalAdjusted()
        );

        return dto;
    }

    public List<StockMovement>
    getProductHistory(Long productId) {

        return repo
                .findByProductProductIdOrderByCreatedAtDesc(
                        productId
                );
    }
}
