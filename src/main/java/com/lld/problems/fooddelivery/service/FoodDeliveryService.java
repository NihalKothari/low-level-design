package com.lld.problems.fooddelivery.service;

import com.lld.common.ErrorCode;
import com.lld.common.Result;
import com.lld.problems.fooddelivery.model.DeliveryAgent;
import com.lld.problems.fooddelivery.model.Order;
import com.lld.problems.fooddelivery.model.OrderStatus;
import com.lld.problems.fooddelivery.model.Restaurant;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FoodDeliveryService {

    private final Map<String, Restaurant> restaurants = new ConcurrentHashMap<>();
    private final Map<String, DeliveryAgent> agents = new ConcurrentHashMap<>();
    private final Map<String, Order> orders = new ConcurrentHashMap<>();

    public void registerRestaurant(Restaurant restaurant) {
        restaurants.put(restaurant.getRestaurantId(), restaurant);
    }

    public void registerDeliveryAgent(DeliveryAgent agent) {
        agents.put(agent.getAgentId(), agent);
    }

    public Result<Order> placeOrder(
            String customerId,
            String restaurantId,
            List<String> items,
            double totalAmount
    ) {
        Restaurant restaurant = restaurants.get(restaurantId);
        if (restaurant == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        if (!restaurant.isOpen()) {
            return Result.failure(ErrorCode.INVALID_INPUT);
        }
        if (items == null || items.isEmpty() || totalAmount <= 0) {
            return Result.failure(ErrorCode.INVALID_INPUT);
        }

        Order order = new Order(
                UUID.randomUUID().toString(),
                customerId,
                restaurantId,
                items,
                totalAmount,
                Instant.now()
        );
        orders.put(order.getOrderId(), order);
        return Result.success(order);
    }

    public Result<Order> confirmOrder(String orderId) {
        return transitionOrder(orderId, OrderStatus.CONFIRMED);
    }

    public Result<Order> markPreparing(String orderId) {
        return transitionOrder(orderId, OrderStatus.PREPARING);
    }

    public Result<Order> assignDeliveryAgent(String orderId, String agentId) {
        Order order = orders.get(orderId);
        if (order == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        if (order.getStatus() != OrderStatus.PREPARING) {
            return Result.failure(ErrorCode.INVALID_INPUT);
        }

        DeliveryAgent agent = agents.get(agentId);
        if (agent == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        if (!agent.isAvailable()) {
            return Result.failure(ErrorCode.CONFLICT);
        }

        agent.setAvailable(false);
        order.setDeliveryAgentId(agentId);
        return Result.success(order);
    }

    public Result<Order> markOutForDelivery(String orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        if (order.getDeliveryAgentId() == null) {
            return Result.failure(ErrorCode.INVALID_INPUT);
        }
        return transitionOrder(orderId, OrderStatus.OUT_FOR_DELIVERY);
    }

    public Result<Order> markDelivered(String orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        Result<Order> result = transitionOrder(orderId, OrderStatus.DELIVERED);
        if (result.isSuccess()) {
            DeliveryAgent agent = agents.get(order.getDeliveryAgentId());
            if (agent != null) {
                agent.setAvailable(true);
            }
        }
        return result;
    }

    public Result<Order> cancelOrder(String orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        if (!order.getStatus().isCancellable()) {
            return Result.failure(ErrorCode.CONFLICT);
        }
        return transitionOrder(orderId, OrderStatus.CANCELLED);
    }

    public Optional<Order> getOrder(String orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }

    public List<Order> getOrdersByCustomer(String customerId) {
        List<Order> result = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getCustomerId().equals(customerId)) {
                result.add(order);
            }
        }
        return result;
    }

    public List<Order> getOrdersByRestaurant(String restaurantId) {
        List<Order> result = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getRestaurantId().equals(restaurantId)) {
                result.add(order);
            }
        }
        return result;
    }

    private Result<Order> transitionOrder(String orderId, OrderStatus nextStatus) {
        Order order = orders.get(orderId);
        if (order == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        if (!order.getStatus().canTransitionTo(nextStatus)) {
            return Result.failure(ErrorCode.INVALID_INPUT);
        }
        order.setStatus(nextStatus);
        return Result.success(order);
    }
}
