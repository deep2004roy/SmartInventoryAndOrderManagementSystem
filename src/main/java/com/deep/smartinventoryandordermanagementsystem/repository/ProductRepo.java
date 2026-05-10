package com.deep.smartinventoryandordermanagementsystem.repository;

import com.deep.smartinventoryandordermanagementsystem.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {
}
