package com.lld.problems.shoppingcart.service;

import com.lld.common.ErrorCode;
import com.lld.common.Result;
import com.lld.common.events.EventBus;
import com.lld.problems.shoppingcart.model.Cart;
import com.lld.problems.shoppingcart.model.CartItem;
import com.lld.problems.shoppingcart.model.Coupon;
import com.lld.problems.shoppingcart.model.OrderPlacedEvent;
import com.lld.problems.shoppingcart.model.Product;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ShoppingCartService {

    private final EventBus eventBus;
    private final Map<String, Product> catalog = new ConcurrentHashMap<>();
    private final Map<String, Coupon> coupons = new ConcurrentHashMap<>();
    private final Map<String, Cart> carts = new ConcurrentHashMap<>();

    public ShoppingCartService(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void registerProduct(Product product) {
        catalog.put(product.getProductId(), product);
    }

    public void registerCoupon(Coupon coupon) {
        coupons.put(coupon.getCode(), coupon);
    }

    public Result<Cart> addItem(String userId, String productId, int quantity) {
        if (quantity <= 0) {
            return Result.failure(ErrorCode.INVALID_INPUT);
        }
        Product product = catalog.get(productId);
        if (product == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        if (!product.hasStock(quantity)) {
            return Result.failure(ErrorCode.OUT_OF_STOCK);
        }

        Cart cart = carts.computeIfAbsent(userId, Cart::new);
        int existingQty = cart.getItems().containsKey(productId)
                ? cart.getItems().get(productId).getQuantity()
                : 0;
        if (!product.hasStock(existingQty + quantity)) {
            return Result.failure(ErrorCode.OUT_OF_STOCK);
        }
        cart.addItem(product, quantity);
        return Result.success(cart);
    }

    public Result<Cart> removeItem(String userId, String productId) {
        Cart cart = carts.get(userId);
        if (cart == null || !cart.getItems().containsKey(productId)) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        cart.removeItem(productId);
        return Result.success(cart);
    }

    public Result<Cart> applyCoupon(String userId, String couponCode) {
        Cart cart = carts.get(userId);
        if (cart == null || cart.isEmpty()) {
            return Result.failure(ErrorCode.INVALID_INPUT);
        }
        Coupon coupon = coupons.get(couponCode);
        if (coupon == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        cart.applyCoupon(coupon);
        return Result.success(cart);
    }

    public Optional<Cart> getCart(String userId) {
        return Optional.ofNullable(carts.get(userId));
    }

    public Result<OrderPlacedEvent> checkout(String userId) {
        Cart cart = carts.get(userId);
        if (cart == null || cart.isEmpty()) {
            return Result.failure(ErrorCode.INVALID_INPUT);
        }

        for (CartItem item : cart.getItems().values()) {
            if (!item.getProduct().hasStock(item.getQuantity())) {
                return Result.failure(ErrorCode.OUT_OF_STOCK);
            }
        }

        Map<String, Integer> lineItems = new HashMap<>();
        for (CartItem item : cart.getItems().values()) {
            item.getProduct().decrementStock(item.getQuantity());
            lineItems.put(item.getProduct().getProductId(), item.getQuantity());
        }

        double subtotal = cart.subtotal();
        double discount = cart.discountAmount();
        double total = cart.total();

        OrderPlacedEvent event = new OrderPlacedEvent(
                UUID.randomUUID().toString(),
                userId,
                lineItems,
                subtotal,
                discount,
                total
        );

        eventBus.publish(event);
        cart.clear();

        return Result.success(event);
    }
}
