package org.example.application.port.in;

import org.example.domain.AccountId;
import org.example.domain.Money;

public interface GetAccountBalanceQuery {
	Money getAccountBalance(AccountId accountId);
}
