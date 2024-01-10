package com.product.service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private int id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private Status status;

    public Product() {
    }

    public Product(String name, String description, double price, int quantity, Status status) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
    }
}
