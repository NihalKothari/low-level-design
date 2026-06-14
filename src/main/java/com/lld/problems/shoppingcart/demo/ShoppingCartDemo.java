package com.lld.problems.shoppingcart.demo;

import com.lld.common.events.InMemoryEventBus;
import com.lld.problems.shoppingcart.model.Coupon;
import com.lld.problems.shoppingcart.model.OrderPlacedEvent;
import com.lld.problems.shoppingcart.model.Product;
import com.lld.problems.shoppingcart.service.ShoppingCartService;

public class ShoppingCartDemo {

    public static void main(String[] args) {
        InMemoryEventBus eventBus = new InMemoryEventBus();
        ShoppingCartService cartService = new ShoppingCartService(eventBus);

        cartService.registerProduct(new Product("P1", "Wireless Mouse", 29.99, 50));
        cartService.registerProduct(new Product("P2", "Mechanical Keyboard", 89.99, 20));
        cartService.registerCoupon(new Coupon("SAVE10", Coupon.DiscountType.PERCENTAGE, 10));

        eventBus.subscribe(OrderPlacedEvent.TOPIC, event -> {
            OrderPlacedEvent order = (OrderPlacedEvent) event;
            System.out.println("Order placed: " + order.getOrderId()
                    + " total=$" + String.format("%.2f", order.getTotal()));
        });

        String userId = "U1";
        cartService.addItem(userId, "P1", 1);
        cartService.addItem(userId, "P2", 1);
        cartService.applyCoupon(userId, "SAVE10");

        OrderPlacedEvent event = cartService.checkout(userId).getValue().orElseThrow();
        System.out.println("Checkout complete for user " + event.getUserId());
        System.out.println("Cart empty after checkout: " + cartService.getCart(userId).map(c -> c.isEmpty()).orElse(true));
    }
}
