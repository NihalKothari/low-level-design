package com.lld.problems.fooddelivery;

import com.lld.common.ErrorCode;
import com.lld.problems.fooddelivery.model.DeliveryAgent;
import com.lld.problems.fooddelivery.model.Order;
import com.lld.problems.fooddelivery.model.OrderStatus;
import com.lld.problems.fooddelivery.model.Restaurant;
import com.lld.problems.fooddelivery.service.FoodDeliveryService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FoodDeliveryTest {

    private FoodDeliveryService service;

    @BeforeEach
    void setUp() {
        service = new FoodDeliveryService();
        service.registerRestaurant(new Restaurant("R1", "Burger Barn", true));
        service.registerDeliveryAgent(new DeliveryAgent("A1", "Sam", true));
    }

    @Test
    void fullOrderLifecycle() {
        Order order = service.placeOrder("C1", "R1", List.of("Burger"), 12.99)
                .getValue().orElseThrow();

        service.confirmOrder(order.getOrderId());
        service.markPreparing(order.getOrderId());
        service.assignDeliveryAgent(order.getOrderId(), "A1");
        service.markOutForDelivery(order.getOrderId());
        Order delivered = service.markDelivered(order.getOrderId()).getValue().orElseThrow();

        assertEquals(OrderStatus.DELIVERED, delivered.getStatus());
        assertTrue(service.getOrder(order.getOrderId()).isPresent());
    }

    @Test
    void rejectOrderFromClosedRestaurant() {
        service.registerRestaurant(new Restaurant("R2", "Closed Kitchen", false));
        assertEquals(
                ErrorCode.INVALID_INPUT,
                service.placeOrder("C1", "R2", List.of("Soup"), 5.0).getError().orElseThrow()
        );
    }

    @Test
    void cancelBeforePreparing() {
        Order order = service.placeOrder("C1", "R1", List.of("Salad"), 8.0)
                .getValue().orElseThrow();
        service.confirmOrder(order.getOrderId());
        Order cancelled = service.cancelOrder(order.getOrderId()).getValue().orElseThrow();
        assertEquals(OrderStatus.CANCELLED, cancelled.getStatus());
    }

    @Test
    void cannotCancelAfterPreparing() {
        Order order = service.placeOrder("C1", "R1", List.of("Pasta"), 15.0)
                .getValue().orElseThrow();
        service.confirmOrder(order.getOrderId());
        service.markPreparing(order.getOrderId());
        assertEquals(ErrorCode.CONFLICT, service.cancelOrder(order.getOrderId()).getError().orElseThrow());
    }

    @Test
    void assignUnavailableAgentFails() {
        Order order1 = service.placeOrder("C1", "R1", List.of("A"), 10.0).getValue().orElseThrow();
        Order order2 = service.placeOrder("C2", "R1", List.of("B"), 11.0).getValue().orElseThrow();

        service.confirmOrder(order1.getOrderId());
        service.markPreparing(order1.getOrderId());
        service.assignDeliveryAgent(order1.getOrderId(), "A1");

        service.confirmOrder(order2.getOrderId());
        service.markPreparing(order2.getOrderId());
        assertEquals(
                ErrorCode.CONFLICT,
                service.assignDeliveryAgent(order2.getOrderId(), "A1").getError().orElseThrow()
        );
    }
}
