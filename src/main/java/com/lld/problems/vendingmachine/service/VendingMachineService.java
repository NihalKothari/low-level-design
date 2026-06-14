package com.lld.problems.vendingmachine.service;

import com.lld.common.ErrorCode;
import com.lld.common.Result;
import com.lld.problems.vendingmachine.model.Coin;
import com.lld.problems.vendingmachine.model.Product;
import com.lld.problems.vendingmachine.model.VendingMachineState;
import java.util.HashMap;
import java.util.Map;

public class VendingMachineService {

    private final Map<String, Product> products = new HashMap<>();
    private VendingMachineState state = VendingMachineState.IDLE;
    private int currentBalanceCents;

    public void addProduct(Product product) {
        products.put(product.getId(), product);
    }

    public void insertCoin(Coin coin) {
        currentBalanceCents += coin.getValueCents();
        if (state == VendingMachineState.IDLE) {
            state = VendingMachineState.SELECTING;
        }
    }

    public Result<Integer> selectProduct(String productId) {
        Product product = products.get(productId);
        if (product == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        if (!product.hasStock()) {
            return Result.failure(ErrorCode.OUT_OF_STOCK);
        }
        if (currentBalanceCents < product.getPriceCents()) {
            return Result.failure(ErrorCode.INSUFFICIENT_FUNDS);
        }

        state = VendingMachineState.DISPENSING;
        int change = currentBalanceCents - product.getPriceCents();
        product.dispense();
        currentBalanceCents = 0;
        state = VendingMachineState.IDLE;
        return Result.success(change);
    }

    public Result<Integer> cancelTransaction() {
        int refund = currentBalanceCents;
        currentBalanceCents = 0;
        state = VendingMachineState.IDLE;
        return Result.success(refund);
    }

    public int getCurrentBalanceCents() {
        return currentBalanceCents;
    }

    public VendingMachineState getState() {
        return state;
    }
}
