package com.lld.problems.atm.model;

import java.util.ArrayList;
import java.util.List;

public class CashDispenser {

    private final int denominationCents;
    private int count;
    private CashDispenser next;

    public CashDispenser(int denominationCents, int count) {
        this.denominationCents = denominationCents;
        this.count = count;
    }

    public void setNext(CashDispenser next) {
        this.next = next;
    }

    public int getDenominationCents() {
        return denominationCents;
    }

    public boolean dispense(long amountCents) {
        if (amountCents == 0) {
            return true;
        }
        if (amountCents % denominationCents == 0 && count > 0) {
            int needed = (int) (amountCents / denominationCents);
            int used = Math.min(needed, count);
            long remaining = amountCents - (long) used * denominationCents;
            count -= used;
            if (remaining == 0) {
                return true;
            }
            if (next != null && next.dispense(remaining)) {
                return true;
            }
            count += used;
            return false;
        }
        if (next != null) {
            return next.dispense(amountCents);
        }
        return false;
    }

    public List<Integer> breakdown(long amountCents) {
        List<Integer> bills = new ArrayList<>();
        breakdownRecursive(amountCents, bills);
        return bills;
    }

    private boolean breakdownRecursive(long amountCents, List<Integer> bills) {
        if (amountCents == 0) {
            return true;
        }
        if (amountCents % denominationCents == 0 && count > 0) {
            int needed = (int) (amountCents / denominationCents);
            int used = Math.min(needed, count);
            for (int i = 0; i < used; i++) {
                bills.add(denominationCents);
            }
            long remaining = amountCents - (long) used * denominationCents;
            if (remaining == 0) {
                return true;
            }
            if (next != null && next.breakdownRecursive(remaining, bills)) {
                return true;
            }
            for (int i = 0; i < used; i++) {
                bills.remove(bills.size() - 1);
            }
            return false;
        }
        if (next != null) {
            return next.breakdownRecursive(amountCents, bills);
        }
        return false;
    }
}
