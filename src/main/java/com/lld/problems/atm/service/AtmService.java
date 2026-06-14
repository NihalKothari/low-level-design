package com.lld.problems.atm.service;

import com.lld.common.ErrorCode;
import com.lld.common.Result;
import com.lld.problems.atm.model.Account;
import com.lld.problems.atm.model.Card;
import com.lld.problems.atm.model.CashDispenser;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtmService {

    private final Map<String, Account> accounts = new HashMap<>();
    private final CashDispenser cashDispenser;
    private Account activeAccount;

    public AtmService(CashDispenser cashDispenser) {
        this.cashDispenser = cashDispenser;
    }

    public void registerAccount(Account account) {
        accounts.put(account.getId(), account);
    }

    public Result<Void> authenticate(Card card, String pin) {
        if (!card.validatePin(pin)) {
            return Result.failure(ErrorCode.UNAUTHORIZED);
        }
        Account account = accounts.get(card.getAccountId());
        if (account == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        activeAccount = account;
        return Result.success(null);
    }

    public Result<Long> getBalance() {
        if (activeAccount == null) {
            return Result.failure(ErrorCode.UNAUTHORIZED);
        }
        return Result.success(activeAccount.getBalanceCents());
    }

    public Result<Void> deposit(long amountCents) {
        if (activeAccount == null) {
            return Result.failure(ErrorCode.UNAUTHORIZED);
        }
        if (amountCents <= 0) {
            return Result.failure(ErrorCode.INVALID_INPUT);
        }
        activeAccount.credit(amountCents);
        return Result.success(null);
    }

    public Result<List<Integer>> withdraw(long amountCents) {
        if (activeAccount == null) {
            return Result.failure(ErrorCode.UNAUTHORIZED);
        }
        if (amountCents <= 0 || amountCents % 500 != 0) {
            return Result.failure(ErrorCode.INVALID_INPUT);
        }
        if (activeAccount.getBalanceCents() < amountCents) {
            return Result.failure(ErrorCode.INSUFFICIENT_FUNDS);
        }
        List<Integer> breakdown = cashDispenser.breakdown(amountCents);
        if (breakdown.isEmpty()) {
            return Result.failure(ErrorCode.OPERATION_FAILED);
        }
        if (!activeAccount.debit(amountCents)) {
            return Result.failure(ErrorCode.INSUFFICIENT_FUNDS);
        }
        if (!cashDispenser.dispense(amountCents)) {
            activeAccount.credit(amountCents);
            return Result.failure(ErrorCode.OPERATION_FAILED);
        }
        return Result.success(breakdown);
    }

    public void endSession() {
        activeAccount = null;
    }
}
