package com.alinaberlin.ecommerceshop.services;

import com.alinaberlin.ecommerceshop.exceptions.InsuficientStockException;
import com.alinaberlin.ecommerceshop.exceptions.InvalidIdException;
import com.alinaberlin.ecommerceshop.models.entities.Cart;
import com.alinaberlin.ecommerceshop.models.entities.CartItem;
import com.alinaberlin.ecommerceshop.models.entities.CartItemId;
import com.alinaberlin.ecommerceshop.models.entities.Product;
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
        Product product = productRepository.findById(productId).orElseThrow(() -> new InvalidIdException("Invalid product id"));
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
        Product product = productRepository.findById(productId).orElseThrow(() -> new InvalidIdException("Invalid product id"));
        CartItemId itemId = new CartItemId(productId, cart.getId());
        CartItem item = cartItemRepository.findById(itemId).orElseThrow(() -> new InvalidIdException("Invalid cart item id"));
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

    @Transactional
    public void clearCart(Long id) {
        Cart cart = cartRepository.findById(id).get();
        cartItemRepository.deleteAll(cart.getItems());
    }

    public Cart findByUserId(long id) {
        return cartRepository.findByUserId(id).orElseThrow(() -> new InvalidIdException("Cart not found"));
    }
}
