package org.example.application.port.in;

import org.example.domain.Account;
import org.example.domain.AccountId;
import org.example.domain.Money;

public class SendMoneyCommand {
    private final AccountId sourceAccountId;
    private final AccountId targetAccountId;
    private final Money money;

    public SendMoneyCommand(AccountId sourceAccountId, AccountId targetAccountId, Money money) {
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.money = money;

        requireNonNull(sourceAccountId);
        requireNonNull(targetAccountId);
        requireNonNull(money);
        requireGreaterThan(money, 0);
    }

    private void requireNonNull(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("null value");
        }
    }

    private void requireGreaterThan(Money money, int target) {

    }
}
