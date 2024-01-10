package com.product.service.controller;

import com.product.service.model.Product;
import com.product.service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findAll());
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") int id) {
        Optional<Product> fProduct = productService.findById(id);
        if (fProduct.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(fProduct.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found product id : " + id);
        }
    }

    @PostMapping("/products")
    public ResponseEntity<?> createNewProduct(@RequestBody Product product) {
        Product tProduct = productService.save(product);
        if (tProduct.getId() > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(tProduct);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to record the product");
        }
    }

    @PutMapping("/product/{PID}")
    public ResponseEntity<?> updateProduct(@PathVariable("PID") int id, @RequestBody Product uProduct) {
        Optional<Product> queryResultProduct = productService.findById(id);
        if (queryResultProduct.isPresent()) {
            Product tempProduct = queryResultProduct.get();
            tempProduct.setName(uProduct.getName());
            tempProduct.setDescription(uProduct.getDescription());
            tempProduct.setPrice(uProduct.getPrice());
            tempProduct.setQuantity(uProduct.getQuantity());
            Product result = productService.save(tempProduct);
            if (result.getId() > 0) {
                return ResponseEntity.status(HttpStatus.OK).body(result);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Update product id : " + id + "unsuccessful");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found product id : " + id);
        }
    }

    @DeleteMapping("/product/{PID}")
    public ResponseEntity<?> deleteProduct(@PathVariable("PID") int id) {
        boolean isSuccess = productService.deleteById(id);
        if (isSuccess) {
            return ResponseEntity.status(HttpStatus.OK).body("The product ID : " + id + "was deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found product id : " + id);
        }
    }

}
