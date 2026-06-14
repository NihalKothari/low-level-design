package com.lld.problems.shoppingcart.model;

public class Coupon {

    public enum DiscountType {
        PERCENTAGE,
        FIXED
    }

    private final String code;
    private final DiscountType type;
    private final double value;

    public Coupon(String code, DiscountType type, double value) {
        this.code = code;
        this.type = type;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public DiscountType getType() {
        return type;
    }

    public double getValue() {
        return value;
    }

    public double calculateDiscount(double subtotal) {
        return switch (type) {
            case PERCENTAGE -> subtotal * (value / 100.0);
            case FIXED -> Math.min(subtotal, value);
        };
    }
}
