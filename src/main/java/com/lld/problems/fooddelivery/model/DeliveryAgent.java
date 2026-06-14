package com.lld.problems.fooddelivery.model;

public class DeliveryAgent {

    private final String agentId;
    private final String name;
    private boolean available;

    public DeliveryAgent(String agentId, String name, boolean available) {
        this.agentId = agentId;
        this.name = name;
        this.available = available;
    }

    public String getAgentId() {
        return agentId;
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
