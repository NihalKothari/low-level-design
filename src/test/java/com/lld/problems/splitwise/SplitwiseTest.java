package com.lld.problems.splitwise;

import com.lld.problems.splitwise.model.Expense;
import com.lld.problems.splitwise.model.Settlement;
import com.lld.problems.splitwise.model.User;
import com.lld.problems.splitwise.service.SplitwiseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SplitwiseTest {

    private SplitwiseService splitwise;

    @BeforeEach
    void setUp() {
        splitwise = new SplitwiseService();
        splitwise.registerUser(new User("U1", "Alice"));
        splitwise.registerUser(new User("U2", "Bob"));
    }

    @Test
    void recordExpenseAndSimplify() {
        assertTrue(splitwise.addExpense(new Expense(
                UUID.randomUUID().toString(),
                "U1",
                300,
                Map.of("U1", 100L, "U2", 200L)
        )).isSuccess());

        assertEquals(200, splitwise.getBalance("U2", "U1"));
        List<Settlement> settlements = splitwise.simplifyBalances();
        assertEquals(1, settlements.size());
        assertEquals(200, settlements.get(0).getAmountCents());
    }

    @Test
    void rejectInvalidSplitTotal() {
        assertTrue(splitwise.addExpense(new Expense(
                UUID.randomUUID().toString(),
                "U1",
                300,
                Map.of("U1", 100L, "U2", 100L)
        )).getError().isPresent());
    }
}
