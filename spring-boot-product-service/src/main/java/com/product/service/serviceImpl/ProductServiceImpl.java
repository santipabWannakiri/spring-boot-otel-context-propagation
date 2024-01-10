package com.product.service.serviceImpl;

import com.product.service.model.Product;
import com.product.service.model.Status;
import com.product.service.repository.ProductRepository;
import com.product.service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        return (List<Product>) productRepository.findByStatus(Status.CURRENT);
    }

    @Override
    public Optional<Product> findById(int id) {
        return productRepository.findById(id);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }


    @Override
    public boolean deleteById(int id) {
        Optional<Product> queryResultProduct = productRepository.findById(id);
        if (queryResultProduct.isPresent()) {
            Product tempProduct = queryResultProduct.get();
            tempProduct.setStatus(Status.DELETE);
            Product result = productRepository.save(tempProduct);
            if (result.getId() > 0 && result.getStatus().equals(Status.DELETE)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
