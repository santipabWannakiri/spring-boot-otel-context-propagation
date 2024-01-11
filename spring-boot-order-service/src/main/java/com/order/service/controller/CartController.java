package com.order.service.controller;

import com.order.service.constants.Constants;
import com.order.service.model.json.AppResponse;
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
            return new ResponseEntity<>(new AppResponse(Constants.SUCCESS_CODE, Constants.SUCCESS_MESSAGE_CODE, Constants.REGISTER_SUCCESS_MESSAGE), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new AppResponse(Constants.INTERNAL_ERROR_CODE, Constants.INTERNAL_MESSAGE_CODE, Constants.UNABLE_TO_PROCESS_MESSAGE), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
