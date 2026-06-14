package com.lld.problems.atm.model;

public class Card {

    private final String cardNumber;
    private final String accountId;
    private final String pin;

    public Card(String cardNumber, String accountId, String pin) {
        this.cardNumber = cardNumber;
        this.accountId = accountId;
        this.pin = pin;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getAccountId() {
        return accountId;
    }

    public boolean validatePin(String enteredPin) {
        return pin.equals(enteredPin);
    }
}
