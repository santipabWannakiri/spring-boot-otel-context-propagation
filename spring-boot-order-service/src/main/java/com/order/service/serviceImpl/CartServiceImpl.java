package com.order.service.serviceImpl;

import com.order.service.model.Cart;
import com.order.service.model.json.ProductInfo;
import com.order.service.model.User;
import com.order.service.repository.CartRepository;
import com.order.service.repository.UserRepository;
import com.order.service.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class CartServiceImpl implements CartService {
    @Value("${PRODUCT_ENDPOINT_SERVICE}")
    private String endpoint;

    @Autowired
    private final RestTemplate restTemplate;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    public CartServiceImpl(RestTemplate restTemplate, CartRepository cartRepository, UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean validateProductStatusAndQuantity(int productId, int quantity) {
        String actualEndpoint = endpoint + "/api/product/" + productId;
        try {
            Optional<ProductInfo> result = Optional.ofNullable(restTemplate.getForObject(actualEndpoint, ProductInfo.class));
            if (result.isPresent() && result.get().getStatus().equals("CURRENT")) {
                if (result.get().getQuantity() > quantity) {
                    return true;
                } else {
                    log.error("Quantity exceed!!");
                    return false;
                }
            } else {
                log.error("Product id : " + productId + " status not active.");
                return false;
            }
        } catch (Exception ex) {
            log.error("Error while validating product: {}", ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean saveProduct(Cart cart) {
        Optional<Cart> result = Optional.of(cartRepository.save(cart));
        return result.isPresent();
    }

    @Override
    public User findUsername(String userName) {
        Optional<User> existingUser = userRepository.findByUsername(userName);
        return existingUser.orElse(null);
    }

    @Override
    public boolean addProduct(int productId, int quantity, String userName) {
        try {
            User existingUser = findUsername(userName);
            if (existingUser == null) {
                log.error("User not found!");
                return false;
            } else {
                if (validateProductStatusAndQuantity(productId, quantity)) {
                    Optional<Cart> existingProduct = cartRepository.findByProductIdAndUserId(productId, existingUser.getId());
                    if (existingProduct.isPresent()) {
                        Cart recordExistingProduct = existingProduct.get();
                        recordExistingProduct.setQuantity(recordExistingProduct.getQuantity() + quantity);
                        return saveProduct(recordExistingProduct);
                    } else {
                        Cart newProduct = new Cart();
                        newProduct.setProductId(productId);
                        newProduct.setQuantity(quantity);
                        newProduct.setLastUpdate("12/12/12");
                        newProduct.setUser(existingUser);
                        return saveProduct(newProduct);
                    }
                } else {
                    log.error("Exceed product quantity or Product status not active.");
                    return false;
                }
            }
        } catch (Exception ex) {
            log.error("Error while process to add user: {}", ex.getMessage());
            return false;
        }
    }

    @Override
    public List<Cart> getAllCarts() {
        return (List<Cart>) cartRepository.findAll();
    }
}
