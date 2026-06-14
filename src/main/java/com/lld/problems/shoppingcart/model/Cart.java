package com.lld.problems.shoppingcart.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class Cart {

    private final String userId;
    private final Map<String, CartItem> items = new LinkedHashMap<>();
    private Coupon appliedCoupon;

    public Cart(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public Map<String, CartItem> getItems() {
        return items;
    }

    public Optional<Coupon> getAppliedCoupon() {
        return Optional.ofNullable(appliedCoupon);
    }

    public void applyCoupon(Coupon coupon) {
        this.appliedCoupon = coupon;
    }

    public void clearCoupon() {
        this.appliedCoupon = null;
    }

    public void addItem(Product product, int quantity) {
        items.merge(
                product.getProductId(),
                new CartItem(product, quantity),
                (existing, incoming) -> {
                    existing.setQuantity(existing.getQuantity() + incoming.getQuantity());
                    return existing;
                }
        );
    }

    public void removeItem(String productId) {
        items.remove(productId);
    }

    public double subtotal() {
        return items.values().stream().mapToDouble(CartItem::lineTotal).sum();
    }

    public double discountAmount() {
        return appliedCoupon == null ? 0.0 : appliedCoupon.calculateDiscount(subtotal());
    }

    public double total() {
        return Math.max(0.0, subtotal() - discountAmount());
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
        appliedCoupon = null;
    }
}
