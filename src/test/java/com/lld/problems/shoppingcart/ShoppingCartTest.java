package com.lld.problems.shoppingcart;

import com.lld.common.ErrorCode;
import com.lld.common.events.InMemoryEventBus;
import com.lld.problems.shoppingcart.model.Coupon;
import com.lld.problems.shoppingcart.model.OrderPlacedEvent;
import com.lld.problems.shoppingcart.model.Product;
import com.lld.problems.shoppingcart.service.ShoppingCartService;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShoppingCartTest {

    private InMemoryEventBus eventBus;
    private ShoppingCartService cartService;

    @BeforeEach
    void setUp() {
        eventBus = new InMemoryEventBus();
        cartService = new ShoppingCartService(eventBus);
        cartService.registerProduct(new Product("P1", "Notebook", 5.0, 100));
        cartService.registerProduct(new Product("P2", "Pen", 2.0, 50));
        cartService.registerCoupon(new Coupon("HALF", Coupon.DiscountType.PERCENTAGE, 50));
    }

    @Test
    void checkoutPublishesOrderPlacedEvent() {
        AtomicReference<OrderPlacedEvent> captured = new AtomicReference<>();
        eventBus.subscribe(OrderPlacedEvent.TOPIC, captured::set);

        cartService.addItem("U1", "P1", 2);
        cartService.addItem("U1", "P2", 1);
        OrderPlacedEvent event = cartService.checkout("U1").getValue().orElseThrow();

        assertEquals(12.0, event.getSubtotal(), 0.001);
        assertEquals("U1", event.getUserId());
        assertEquals(event.getOrderId(), captured.get().getOrderId());
        assertTrue(cartService.getCart("U1").map(c -> c.isEmpty()).orElse(false));
    }

    @Test
    void applyCouponReducesTotal() {
        eventBus.subscribe(OrderPlacedEvent.TOPIC, e -> { });

        cartService.addItem("U1", "P1", 4);
        cartService.applyCoupon("U1", "HALF");
        OrderPlacedEvent event = cartService.checkout("U1").getValue().orElseThrow();

        assertEquals(20.0, event.getSubtotal(), 0.001);
        assertEquals(10.0, event.getDiscount(), 0.001);
        assertEquals(10.0, event.getTotal(), 0.001);
    }

    @Test
    void rejectCheckoutOnEmptyCart() {
        assertEquals(ErrorCode.INVALID_INPUT, cartService.checkout("U1").getError().orElseThrow());
    }

    @Test
    void rejectWhenInsufficientStock() {
        cartService.addItem("U1", "P2", 50);
        assertEquals(ErrorCode.OUT_OF_STOCK, cartService.addItem("U1", "P2", 1).getError().orElseThrow());
    }

    @Test
    void decrementStockOnCheckout() {
        eventBus.subscribe(OrderPlacedEvent.TOPIC, e -> { });

        cartService.addItem("U1", "P1", 3);
        cartService.checkout("U1");

        assertEquals(ErrorCode.OUT_OF_STOCK, cartService.addItem("U2", "P1", 98).getError().orElseThrow());
        assertTrue(cartService.addItem("U2", "P1", 97).isSuccess());
    }
}
