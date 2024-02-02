package com.alinaberlin.ecommerceshop.controllers;

import com.alinaberlin.ecommerceshop.models.Cart;
import com.alinaberlin.ecommerceshop.models.CartItem;
import com.alinaberlin.ecommerceshop.models.User;
import com.alinaberlin.ecommerceshop.payloads.Item;
import com.alinaberlin.ecommerceshop.repositories.UserRepository;
import com.alinaberlin.ecommerceshop.services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/cart")
public class CartController {

    public final CartService cartService;

    private final UserRepository userRepository;

    public CartController(CartService cartService, UserRepository userRepository) {
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Cart> createCart(Principal principal) {
        User user = userRepository.findUserByEmail(principal.getName()).orElseThrow();
        Cart cart = cartService.createCart(new Cart(user));
        return new ResponseEntity<>(cart, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Cart> getCart(Principal principal) {
        User user = userRepository.findUserByEmail(principal.getName()).orElseThrow();
        Cart cart = cartService.findByUserId(user.getId());
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/item/add")
    public ResponseEntity<CartItem> addItem(Principal principal, @RequestBody Item item) {
        User user = userRepository.findUserByEmail(principal.getName()).orElseThrow();
        return ResponseEntity.ok(cartService.addItem(user.getId(), item.productId(), item.quantity()));
    }

    @PostMapping("/item/update")
    public ResponseEntity<CartItem> updateItem(Principal principal, @RequestBody Item item) {
        User user = userRepository.findUserByEmail(principal.getName()).orElseThrow();
        return ResponseEntity.ok(cartService.updateItem(user.getId(), item.productId(), item.quantity()));
    }

    @DeleteMapping("/item/{productId}")
    public ResponseEntity<?> deleteItem(Principal principal, @PathVariable Long productId) {
        User user = userRepository.findUserByEmail(principal.getName()).orElseThrow();
        cartService.deleteItem(user.getId(), productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
