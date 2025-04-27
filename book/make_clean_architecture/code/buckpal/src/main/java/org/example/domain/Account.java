package org.example.domain;

public class Account {
    private AccountId id;
    private Money baselineBalance;
    private ActivityWindow activityWindow;

    public Money calculateBalance() {
        return null;
    }

    public boolean withdraw(Money money, AccountId targetAccountId) {
        return false;
    }

    public boolean deposit() {
        return false;
    }

    public static class AccountId {
        private Long id;
    }
}
