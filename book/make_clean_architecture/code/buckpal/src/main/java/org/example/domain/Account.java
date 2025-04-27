package org.example.domain;

public class Account {
    private AccountId id;
    private Money baselineBalance;
    private ActivityWindow activityWindow;

    public Money calculateBalance() {
        // todo
        return null;
    }

    public boolean withdraw(Money money, AccountId targetAccountId) {
        // todo
        return false;
    }

    public boolean deposit() {
        // todo
        return false;
    }
}
