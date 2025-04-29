package org.example.application.service;

import org.example.application.port.in.SendMoneyCommand;
import org.example.application.port.in.SendMoneyUseCase;
import org.example.domain.AccountId;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SendMoneyService implements SendMoneyUseCase {
	@Override
	public boolean sendMoney(SendMoneyCommand command) {
		requireAccountExists(command.getSourceAccountId());
		requireAccountExists(command.getTargetAccountId());

		// todo
		return false;
	}

	private void requireAccountExists(AccountId accountId) {
		// todo
	}
}
