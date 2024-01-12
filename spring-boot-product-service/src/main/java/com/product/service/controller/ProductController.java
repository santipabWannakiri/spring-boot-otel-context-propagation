package com.product.service.controller;

import com.product.service.constants.Constants;
import com.product.service.model.Product;
import com.product.service.model.json.AppResponse;
import com.product.service.model.json.QuantityRequest;
import com.product.service.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts() {
        Optional<List<Product>> result = Optional.ofNullable(productService.findAll());
        if (result.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(result.get());
        } else {
            return new ResponseEntity<>(Constants.INTERNAL_ERROR_RESPONSE_OBJECT, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") int id) {
        Optional<Product> result = Optional.ofNullable(productService.findById(id));
        if (result.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(result.get());
        } else {
            return new ResponseEntity<>(Constants.INTERNAL_ERROR_RESPONSE_OBJECT, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/products")
    public ResponseEntity<?> createNewProduct(@RequestBody Product product) {
        Optional<Product> result = Optional.ofNullable(productService.save(product));
        if (result.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(result.get());
        } else {
            return new ResponseEntity<>(Constants.INTERNAL_ERROR_RESPONSE_OBJECT, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/product/{PID}")
    public ResponseEntity<?> updateProduct(@PathVariable("PID") int productId, @RequestBody Product uProduct) {
        if (productService.updateProduct(productId, uProduct)) {
            return new ResponseEntity<>(new AppResponse(Constants.UPDATE_SUCCESS_MESSAGE), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Constants.INTERNAL_ERROR_RESPONSE_OBJECT, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/product/{PID}")
    public ResponseEntity<?> deleteProduct(@PathVariable("PID") int id) {
        if (productService.deleteById(id)) {
            return new ResponseEntity<>(new AppResponse(Constants.DELETE_SUCCESS_MESSAGE), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Constants.INTERNAL_ERROR_RESPONSE_OBJECT, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/product/deduct/{PID}")
    public ResponseEntity<?> deductQuantity(@PathVariable("PID") int productId, @RequestBody QuantityRequest quantity) {
        if (productService.deductQuantity(productId, quantity.getRequestedQuantity())) {
            return new ResponseEntity<>(new AppResponse(Constants.DEDUCT_SUCCESS_MESSAGE), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Constants.INTERNAL_ERROR_RESPONSE_OBJECT, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
