package com.order.service.controller;

import com.order.service.model.Cart;
import com.order.service.model.json.CartRequest;
import com.order.service.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/carts")
    public ResponseEntity<?> getCarts() {
        List<Cart> result = cartService.getAllCarts();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/cart")
    public ResponseEntity<?> addProductToCart(@RequestBody CartRequest request) {
        if (cartService.addProduct(request.getProductId(), request.getQuantity(), request.getUsername())) {
            return ResponseEntity.status(HttpStatus.OK).body("Added product id : " + request.getProductId());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to add product id : " + request.getProductId());
        }
    }

}
