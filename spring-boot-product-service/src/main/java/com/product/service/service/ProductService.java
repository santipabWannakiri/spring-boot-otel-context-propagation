package com.product.service.service;

import com.product.service.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    public List<Product> findAll();

    public Optional<Product> findById(int id);

    public Product save(Product newProduct);

    public boolean deleteById(int id);

}
