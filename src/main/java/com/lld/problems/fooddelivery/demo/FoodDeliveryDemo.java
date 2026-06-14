package com.lld.problems.fooddelivery.demo;

import com.lld.problems.fooddelivery.model.DeliveryAgent;
import com.lld.problems.fooddelivery.model.Order;
import com.lld.problems.fooddelivery.model.OrderStatus;
import com.lld.problems.fooddelivery.model.Restaurant;
import com.lld.problems.fooddelivery.service.FoodDeliveryService;
import java.util.List;

public class FoodDeliveryDemo {

    public static void main(String[] args) {
        FoodDeliveryService service = new FoodDeliveryService();
        service.registerRestaurant(new Restaurant("R1", "Pizza Palace", true));
        service.registerDeliveryAgent(new DeliveryAgent("A1", "Alex Rider", true));

        Order order = service.placeOrder(
                "C1",
                "R1",
                List.of("Margherita Pizza", "Garlic Bread"),
                24.99
        ).getValue().orElseThrow();

        System.out.println("Order placed: " + order.getOrderId() + " status=" + order.getStatus());

        service.confirmOrder(order.getOrderId());
        service.markPreparing(order.getOrderId());
        service.assignDeliveryAgent(order.getOrderId(), "A1");
        service.markOutForDelivery(order.getOrderId());
        Order delivered = service.markDelivered(order.getOrderId()).getValue().orElseThrow();

        System.out.println("Delivered: " + delivered.getOrderId() + " status=" + delivered.getStatus());
        System.out.println("Final status is DELIVERED: " + (delivered.getStatus() == OrderStatus.DELIVERED));
    }
}
