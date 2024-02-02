package com.alinaberlin.ecommerceshop.services;

import com.alinaberlin.ecommerceshop.exceptions.InsuficientStockException;
import com.alinaberlin.ecommerceshop.exceptions.InvalidIdException;
import com.alinaberlin.ecommerceshop.models.Cart;
import com.alinaberlin.ecommerceshop.models.CartItem;
import com.alinaberlin.ecommerceshop.models.CartItemId;
import com.alinaberlin.ecommerceshop.models.Product;
import com.alinaberlin.ecommerceshop.repositories.CartItemRepository;
import com.alinaberlin.ecommerceshop.repositories.CartRepository;
import com.alinaberlin.ecommerceshop.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    private final ProductRepository productRepository;


    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Transactional
    public CartItem addItem(Long userId, Long productId, int quantity) {
        Cart cart = findByUserId(userId);
        Product product = productRepository.findById(productId).orElseThrow();
        if (product.getQuantity() >= quantity) {
            product.setQuantity(product.getQuantity() - quantity);
            productRepository.save(product);
            CartItemId itemId = new CartItemId(productId, cart.getId());
            CartItem item = new CartItem(itemId, quantity, product);
            return cartItemRepository.save(item);
        }
        throw new InsuficientStockException("Product doesn't has enough quantity");
    }

    @Transactional
    public CartItem updateItem(Long userId, Long productId, int quantity) {
        Cart cart = findByUserId(userId);
        Product product = productRepository.findById(productId).orElseThrow();
        CartItemId itemId = new CartItemId(productId, cart.getId());
        CartItem item = cartItemRepository.findById(itemId).orElseThrow();
        int diff = quantity - item.getQuantity();
        if (product.getQuantity() >= diff) {
            product.setQuantity(product.getQuantity() - diff);
            productRepository.save(product);
            item.setQuantity(quantity);
            return cartItemRepository.save(item);
        }
        throw new InsuficientStockException("Product doesn't has enough quantity");
    }

    @Transactional
    public void deleteItem(Long userId, Long productId) {
        Cart cart = findByUserId(userId);
        CartItem cartItem = cartItemRepository.findById(new CartItemId(productId, cart.getId()))
                .orElseThrow(() -> new InvalidIdException("Item doesn't exists"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new InvalidIdException("Product doesn't exists"));
        product.setQuantity(product.getQuantity() + cartItem.getQuantity());
        productRepository.save(product);
        cartItemRepository.delete(cartItem);
    }

    public Cart findByUserId(long id) {
        return cartRepository.findByUserId(id).orElseThrow(() -> new InvalidIdException("Cart not found"));
    }
}
