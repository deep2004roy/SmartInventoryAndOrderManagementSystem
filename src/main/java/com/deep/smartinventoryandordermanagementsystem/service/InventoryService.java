package com.deep.smartinventoryandordermanagementsystem.service;

import com.deep.smartinventoryandordermanagementsystem.dto.inventory.InventoryAlertDTO;
import com.deep.smartinventoryandordermanagementsystem.dto.inventory.OutOfStockProductDTO;
import com.deep.smartinventoryandordermanagementsystem.model.Product;
import com.deep.smartinventoryandordermanagementsystem.repository.ProductRepo;

import java.util.List;

public class InventoryService {
    private final ProductRepo productRepo;

    public InventoryService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public List<InventoryAlertDTO> getInventoryAlerts() {

        List<Product> products =
                productRepo.findProductsBelowReorderLevel();

        return products.stream()
                .map(product -> {

                    InventoryAlertDTO dto =
                            new InventoryAlertDTO();

                    dto.setProductId(product.getProductId());
                    dto.setSku(product.getSku());
                    dto.setProductName(product.getName());
                    dto.setCurrentStock(product.getQuantity());
                    dto.setReorderLevel(product.getReorderLevel());

                    return dto;

                }).toList();
    }

    public List<OutOfStockProductDTO> getOutOfStockProducts() {

        return productRepo.findOutOfStockProducts()
                .stream()
                .map(product -> {

                    OutOfStockProductDTO dto =
                            new OutOfStockProductDTO();

                    dto.setProductId(product.getProductId());
                    dto.setSku(product.getSku());
                    dto.setProductName(product.getName());

                    return dto;

                }).toList();
    }
}
