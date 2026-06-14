package com.lld.problems.vendingmachine.demo;

import com.lld.problems.vendingmachine.model.Coin;
import com.lld.problems.vendingmachine.model.Product;
import com.lld.problems.vendingmachine.service.VendingMachineService;

public class VendingMachineDemo {

    public static void main(String[] args) {
        VendingMachineService machine = new VendingMachineService();
        machine.addProduct(new Product("P1", "Soda", 75, 5));

        machine.insertCoin(Coin.QUARTER);
        machine.insertCoin(Coin.QUARTER);
        machine.insertCoin(Coin.QUARTER);
        int change = machine.selectProduct("P1").getValue().orElseThrow();
        System.out.println("Dispensed Soda. Change: " + change + " cents");
    }
}
