package com.lld.problems.vendingmachine.model;

public class Product {

    private final String id;
    private final String name;
    private final int priceCents;
    private int stock;

    public Product(String id, String name, int priceCents, int stock) {
        this.id = id;
        this.name = name;
        this.priceCents = priceCents;
        this.stock = stock;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPriceCents() {
        return priceCents;
    }

    public int getStock() {
        return stock;
    }

    public boolean hasStock() {
        return stock > 0;
    }

    public void dispense() {
        if (stock > 0) {
            stock--;
        }
    }
}
