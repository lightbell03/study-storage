package org.example.adapter.web;

import org.example.application.port.in.SendMoneyCommand;
import org.example.application.port.in.SendMoneyUseCase;
import org.example.domain.AccountId;
import org.example.domain.Money;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SendMoneyController {
	private final SendMoneyUseCase sendMoneyUseCase;

	@PostMapping("/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}")
	public void sendMoney(
		@PathVariable("sourceAccountId") Long sourceAccountId,
		@PathVariable("targetAccountId") Long targetAccountId,
		@PathVariable("amount") Long amount) {
		SendMoneyCommand sendMoneyCommand = new SendMoneyCommand(
			new AccountId(sourceAccountId),
			new AccountId(targetAccountId),
			Money.of(amount)
		);

		sendMoneyUseCase.sendMoney(sendMoneyCommand);
	}
}
