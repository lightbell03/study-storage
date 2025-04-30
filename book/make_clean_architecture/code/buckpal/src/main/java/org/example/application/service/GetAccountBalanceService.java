package org.example.application.service;

import org.example.application.port.in.GetAccountBalanceQuery;
import org.example.application.port.out.LoadAccountPort;
import org.example.domain.AccountId;
import org.example.domain.Money;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetAccountBalanceService implements GetAccountBalanceQuery {

	private final LoadAccountPort loadAccountPort;

	@Override
	public Money getAccountBalance(AccountId accountId) {
		// todo
		return null;
	}
}
