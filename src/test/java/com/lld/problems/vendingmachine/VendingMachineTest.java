package com.lld.problems.vendingmachine;

import com.lld.problems.vendingmachine.model.Coin;
import com.lld.problems.vendingmachine.model.Product;
import com.lld.problems.vendingmachine.model.VendingMachineState;
import com.lld.problems.vendingmachine.service.VendingMachineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VendingMachineTest {

    private VendingMachineService machine;

    @BeforeEach
    void setUp() {
        machine = new VendingMachineService();
        machine.addProduct(new Product("P1", "Chips", 50, 3));
    }

    @Test
    void purchaseWithExactChange() {
        machine.insertCoin(Coin.QUARTER);
        machine.insertCoin(Coin.QUARTER);
        int change = machine.selectProduct("P1").getValue().orElseThrow();
        assertEquals(0, change);
        assertEquals(VendingMachineState.IDLE, machine.getState());
    }

    @Test
    void rejectInsufficientFunds() {
        machine.insertCoin(Coin.QUARTER);
        assertTrue(machine.selectProduct("P1").getError().isPresent());
    }
}
