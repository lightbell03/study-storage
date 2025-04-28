package org.example.application.port.in;

import jakarta.validation.constraints.NotNull;
import org.example.domain.AccountId;
import org.example.domain.Money;

public class SendMoneyCommand {
    @NotNull
    private final AccountId sourceAccountId;
    @NotNull
    private final AccountId targetAccountId;
    @NotNull
    private final Money money;

    public SendMoneyCommand(AccountId sourceAccountId, AccountId targetAccountId, Money money) {
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.money = money;

        requireGreaterThan(money, 0);
    }

    private void requireGreaterThan(Money money, int target) {
        // todo
    }
}
