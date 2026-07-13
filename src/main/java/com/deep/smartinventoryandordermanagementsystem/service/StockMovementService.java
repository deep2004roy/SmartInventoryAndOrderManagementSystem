package com.deep.smartinventoryandordermanagementsystem.service;

import com.deep.smartinventoryandordermanagementsystem.model.Product;
import com.deep.smartinventoryandordermanagementsystem.model.StockMovement;
import com.deep.smartinventoryandordermanagementsystem.model.StockMovementType;
import com.deep.smartinventoryandordermanagementsystem.repository.StockMovementRepository;
import org.springframework.stereotype.Service;

@Service
public class StockMovementService {
    private final StockMovementRepository stockMovementRepository;

    public StockMovementService(StockMovementRepository stockMovementRepository) {
        this.stockMovementRepository = stockMovementRepository;
    }

    public void recordMovement(Product product,
                               int quantityChanged,
                               StockMovementType type,
                               String reference) {

        StockMovement movement = new StockMovement();
        movement.setProduct(product);
        movement.setQuantityChanged(quantityChanged);
        movement.setQuantityAfter(product.getQuantity());
        movement.setType(type);
        movement.setReference(reference);

        stockMovementRepository.save(movement);
    }
}
