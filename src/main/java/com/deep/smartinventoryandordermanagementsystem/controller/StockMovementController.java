package com.deep.smartinventoryandordermanagementsystem.controller;

import com.deep.smartinventoryandordermanagementsystem.model.StockMovement;
import com.deep.smartinventoryandordermanagementsystem.repository.StockMovementRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/stock")
public class StockMovementController {
    private final StockMovementRepository repo;

    public StockMovementController(StockMovementRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/history/{productId}")
    public ResponseEntity<List<StockMovement>> getHistory(@PathVariable Long productId) {

        List<StockMovement> history =
                repo.findByProductProductIdOrderByCreatedAtDesc(productId);

        if (history.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(history);
    }
}
