package org.example.application.service;

import org.example.application.port.in.GetAccountBalanceQuery;
import org.example.domain.AccountId;
import org.example.domain.Money;

public class GetAccountBalanceService implements GetAccountBalanceQuery {
	@Override
	public Money getAccountBalance(AccountId accountId) {
		return null;
	}
}
