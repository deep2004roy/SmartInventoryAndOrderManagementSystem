package com.deep.smartinventoryandordermanagementsystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Product {
    @Id
    private int id;
    private String name;
    private String description;
    private int price;
    private int quantity;
    private String category;
}
