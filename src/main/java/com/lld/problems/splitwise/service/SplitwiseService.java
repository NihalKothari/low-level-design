package com.lld.problems.splitwise.service;

import com.lld.common.ErrorCode;
import com.lld.common.Result;
import com.lld.problems.splitwise.model.Expense;
import com.lld.problems.splitwise.model.Settlement;
import com.lld.problems.splitwise.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplitwiseService {

    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Long> balances = new HashMap<>();

    public void registerUser(User user) {
        users.put(user.getId(), user);
    }

    public Result<Void> addExpense(Expense expense) {
        if (!users.containsKey(expense.getPaidByUserId())) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        long splitSum = expense.getSplits().values().stream().mapToLong(Long::longValue).sum();
        if (splitSum != expense.getTotalCents()) {
            return Result.failure(ErrorCode.INVALID_INPUT);
        }
        for (Map.Entry<String, Long> entry : expense.getSplits().entrySet()) {
            if (!users.containsKey(entry.getKey())) {
                return Result.failure(ErrorCode.NOT_FOUND);
            }
            String from = entry.getKey();
            String to = expense.getPaidByUserId();
            if (!from.equals(to)) {
                adjustBalance(from, to, entry.getValue());
            }
        }
        return Result.success(null);
    }

    public long getBalance(String fromUserId, String toUserId) {
        return balances.getOrDefault(balanceKey(fromUserId, toUserId), 0L);
    }

    public Map<String, Long> getAllBalances() {
        return Map.copyOf(balances);
    }

    public List<Settlement> simplifyBalances() {
        Map<String, Long> net = new HashMap<>();
        for (User user : users.values()) {
            net.put(user.getId(), 0L);
        }
        for (Map.Entry<String, Long> entry : balances.entrySet()) {
            String[] parts = entry.getKey().split("->");
            String from = parts[0];
            String to = parts[1];
            long amount = entry.getValue();
            net.merge(from, amount, Long::sum);
            net.merge(to, -amount, Long::sum);
        }

        List<String> debtors = new ArrayList<>();
        List<Long> debtorAmounts = new ArrayList<>();
        List<String> creditors = new ArrayList<>();
        List<Long> creditorAmounts = new ArrayList<>();
        for (Map.Entry<String, Long> entry : net.entrySet()) {
            if (entry.getValue() > 0) {
                debtors.add(entry.getKey());
                debtorAmounts.add(entry.getValue());
            } else if (entry.getValue() < 0) {
                creditors.add(entry.getKey());
                creditorAmounts.add(-entry.getValue());
            }
        }

        List<Settlement> settlements = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (i < debtors.size() && j < creditors.size()) {
            long pay = Math.min(debtorAmounts.get(i), creditorAmounts.get(j));
            if (pay > 0) {
                settlements.add(new Settlement(debtors.get(i), creditors.get(j), pay));
            }
            debtorAmounts.set(i, debtorAmounts.get(i) - pay);
            creditorAmounts.set(j, creditorAmounts.get(j) - pay);
            if (debtorAmounts.get(i) == 0) {
                i++;
            }
            if (creditorAmounts.get(j) == 0) {
                j++;
            }
        }
        return settlements;
    }

    private void adjustBalance(String from, String to, long amountCents) {
        String key = balanceKey(from, to);
        balances.merge(key, amountCents, Long::sum);
    }

    private String balanceKey(String from, String to) {
        return from + "->" + to;
    }
}
