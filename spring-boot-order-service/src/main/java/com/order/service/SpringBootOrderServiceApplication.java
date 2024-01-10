package com.order.service;

import com.order.service.model.Cart;
import com.order.service.model.User;
import com.order.service.repository.CartRepository;
import com.order.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class SpringBootOrderServiceApplication {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    public SpringBootOrderServiceApplication(CartRepository cartRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootOrderServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(String[] args) {
        return r -> {
            initialObject();
        };
    }

    private void initialObject() {
// Create users
        User user1 = new User();
        user1.setUsername("santipab");
        user1.setEmail("san@gmail.com");

        User user2 = new User();
        user2.setUsername("jenny");
        user2.setEmail("jen@gmail.com");

// Save users first
        userRepository.saveAll(Arrays.asList(user1, user2));

// Create cart entities with the associated users
        Cart U1P1 = new Cart(112, 99, "12/12/2024", user1);
        Cart U1P2 = new Cart(113, 1, "11/11/2024", user1);
        Cart U1P3 = new Cart(114, 20, "10/10/2024", user1);
        Cart U1P4 = new Cart(112, 99, "01/01/2024", user1);

        Cart U2P2 = new Cart(113, 1, "11/11/2024", user2);
        Cart U2P3 = new Cart(114, 20, "10/10/2024", user2);
        Cart U2P4 = new Cart(112, 99, "01/01/2024", user2);

// Save cart entities using CartRepository
        cartRepository.saveAll(Arrays.asList(U1P1, U1P2, U1P3, U1P4, U2P2, U2P3, U2P4));

    }
}
