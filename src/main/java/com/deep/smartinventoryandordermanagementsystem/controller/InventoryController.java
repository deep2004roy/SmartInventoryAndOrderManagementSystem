package com.deep.smartinventoryandordermanagementsystem.controller;

import com.deep.smartinventoryandordermanagementsystem.dto.inventory.InventoryAlertDTO;
import com.deep.smartinventoryandordermanagementsystem.dto.inventory.OutOfStockProductDTO;
import com.deep.smartinventoryandordermanagementsystem.service.InventoryService;
import com.deep.smartinventoryandordermanagementsystem.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/inventory")
@PreAuthorize("hasRole('ADMIN')")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/alerts")
    public ResponseEntity<List<InventoryAlertDTO>>
    getInventoryAlerts() {

        return ResponseEntity.ok(
                inventoryService.getInventoryAlerts()
        );
    }

    @GetMapping("/out-of-stock")
    public ResponseEntity<List<OutOfStockProductDTO>>
    getOutOfStockProducts() {

        return ResponseEntity.ok(
                inventoryService.getOutOfStockProducts()
        );
    }
}
