package com.order.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "carts")
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(hidden = true)
    private int id;
    private int productId;
    private int quantity;
    private String lastUpdate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("carts")
    private User user;
    public Cart() {
    }

    public Cart(int productId, int quantity, String lastUpdate, User user) {
        this.productId = productId;
        this.quantity = quantity;
        this.lastUpdate = lastUpdate;
        this.user = user;
    }

    public Cart(int productId, int quantity, String lastUpdate) {
        this.productId = productId;
        this.quantity = quantity;
        this.lastUpdate = lastUpdate;
    }

}
