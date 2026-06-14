package com.lld.problems.atm.demo;

import com.lld.problems.atm.model.Account;
import com.lld.problems.atm.model.Card;
import com.lld.problems.atm.model.CashDispenser;
import com.lld.problems.atm.service.AtmService;
import java.util.List;

public class AtmDemo {

    public static void main(String[] args) {
        CashDispenser twenties = new CashDispenser(2000, 10);
        CashDispenser tens = new CashDispenser(1000, 10);
        CashDispenser fives = new CashDispenser(500, 10);
        twenties.setNext(tens);
        tens.setNext(fives);

        AtmService atm = new AtmService(twenties);
        Account account = new Account("A1", 10000);
        atm.registerAccount(account);

        Card card = new Card("4111", "A1", "1234");
        if (!atm.authenticate(card, "1234").isSuccess()) {
            throw new IllegalStateException("Authentication failed");
        }
        List<Integer> bills = atm.withdraw(5000).getValue().orElseThrow();
        System.out.println("Withdrew $50 as bills (cents): " + bills);
        System.out.println("Balance: " + atm.getBalance().getValue().orElseThrow() + " cents");
    }
}
