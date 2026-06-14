package com.lld.problems.atm;

import com.lld.problems.atm.model.Account;
import com.lld.problems.atm.model.Card;
import com.lld.problems.atm.model.CashDispenser;
import com.lld.problems.atm.service.AtmService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AtmTest {

    private AtmService atm;
    private Card card;

    @BeforeEach
    void setUp() {
        CashDispenser twenties = new CashDispenser(2000, 5);
        CashDispenser tens = new CashDispenser(1000, 5);
        CashDispenser fives = new CashDispenser(500, 5);
        twenties.setNext(tens);
        tens.setNext(fives);

        atm = new AtmService(twenties);
        atm.registerAccount(new Account("A1", 20000));
        card = new Card("4111", "A1", "1234");
        assertTrue(atm.authenticate(card, "1234").isSuccess());
    }

    @Test
    void withdrawDispensesBills() {
        List<Integer> bills = atm.withdraw(5000).getValue().orElseThrow();
        assertEquals(5000, bills.stream().mapToInt(Integer::intValue).sum());
        assertEquals(15000, atm.getBalance().getValue().orElseThrow());
    }

    @Test
    void rejectInvalidPin() {
        atm.endSession();
        assertTrue(atm.authenticate(card, "0000").getError().isPresent());
    }
}
