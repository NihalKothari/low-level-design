package com.lld.problems.splitwise.demo;

import com.lld.problems.splitwise.model.Expense;
import com.lld.problems.splitwise.model.Settlement;
import com.lld.problems.splitwise.model.User;
import com.lld.problems.splitwise.service.SplitwiseService;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SplitwiseDemo {

    public static void main(String[] args) {
        SplitwiseService splitwise = new SplitwiseService();
        splitwise.registerUser(new User("U1", "Alice"));
        splitwise.registerUser(new User("U2", "Bob"));

        Expense expense = new Expense(
                UUID.randomUUID().toString(),
                "U1",
                200,
                Map.of("U1", 100L, "U2", 100L)
        );
        if (!splitwise.addExpense(expense).isSuccess()) {
            throw new IllegalStateException("Failed to add expense");
        }

        List<Settlement> settlements = splitwise.simplifyBalances();
        Settlement settlement = settlements.get(0);
        System.out.println(settlement.getFromUserId() + " owes " + settlement.getToUserId()
                + " " + settlement.getAmountCents() + " cents");
    }
}
