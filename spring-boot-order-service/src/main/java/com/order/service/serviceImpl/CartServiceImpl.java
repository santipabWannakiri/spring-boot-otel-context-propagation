package com.order.service.serviceImpl;

import com.order.service.constants.Constants;
import com.order.service.exception.type.*;
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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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


    private boolean validateProductStatusAndQuantity(int productId, int quantity) {
        String actualEndpoint = endpoint + "/api/product/" + productId;
        try {
            Optional<ProductInfo> result = Optional.ofNullable(restTemplate.getForObject(actualEndpoint, ProductInfo.class));
            if (result.isPresent() && result.get().getStatus() != null && result.get().getStatus().equals("CURRENT")) {
                if (result.get().getQuantity() > quantity) {
                    return true;
                } else {
                    log.error("Quantity exceed!!");
                    throw new QuantityExceedException(Constants.QUANTITY_EXCEED_MESSAGE);
                }
            } else {
                log.error("Product id : " + productId + " status not active.");
                throw new ProductStatusNotActiveException(Constants.PRODUCT_STATUS_NOT_ACTIVE_MESSAGE);
            }
        } catch (RestClientException ex) {
            log.error("Error during REST call: " + ex.getMessage());
            throw new UnableToConnectToEndpointException(Constants.UNABLE_CONNECT_TO_ENDPOINT_MESSAGE);
        }
    }


    private boolean updateExistingProduct(Cart existingProduct, int quantity, String nowDate) {
        existingProduct.setQuantity(existingProduct.getQuantity() + quantity);
        existingProduct.setUpdatedAt(nowDate);
        return saveProduct(existingProduct);
    }


    private boolean createNewProduct(User existingUser, int productId, int quantity, String nowDate) {
        Cart newProduct = new Cart();
        newProduct.setProductId(productId);
        newProduct.setQuantity(quantity);
        newProduct.setCreatedAt(nowDate);
        newProduct.setUpdatedAt(nowDate);
        newProduct.setUser(existingUser);
        return saveProduct(newProduct);
    }


    private boolean saveProduct(Cart cart) {
        Optional<Cart> result = Optional.of(cartRepository.save(cart));
        return result.isPresent();
    }


    private User findUserOrThrow(String userName) {
        User existingUser = userRepository.findByUsername(userName);
        if (existingUser == null) {
            log.error("User not found!");
            throw new UserNotFoundException(Constants.USER_NOT_FOUND_MESSAGE);
        }
        return existingUser;
    }

    @Override
    public boolean addProduct(int productId, int quantity, String userName) {
        String nowDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).toString();
        try {
            User existingUser = findUserOrThrow(userName);
            validateProductStatusAndQuantity(productId, quantity);
            Optional<Cart> existingProduct = cartRepository.findByProductIdAndUserId(productId, existingUser.getId());
            if (existingProduct.isPresent()) {
                return updateExistingProduct(existingProduct.get(), quantity, nowDate);
            } else {
                return createNewProduct(existingUser, productId, quantity, nowDate);
            }
        } catch (Exception ex) {
            log.error("Error while saving the product : " + productId);
            throw new UnableToAddProductException(Constants.UNABLE_TO_SAVE_PRODUCT_MESSAGE);
        }
    }

    @Override
    public List<Cart> getAllCarts() {
        return (List<Cart>) cartRepository.findAll();
    }
}
