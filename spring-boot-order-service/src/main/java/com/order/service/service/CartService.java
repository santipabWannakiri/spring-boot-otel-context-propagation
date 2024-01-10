package com.order.service.service;

import com.order.service.model.Cart;
import com.order.service.model.User;

import java.util.List;

public interface CartService {

    public boolean validateProductStatusAndQuantity(int productId, int quantity);

    public boolean saveProduct(Cart cart);

    public User findUsername(String userName);

    public boolean addProduct(int productId ,int quantity , String userName);

    public List<Cart> getAllCarts();
}
