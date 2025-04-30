package org.example.adapter.web;

import java.util.List;

import org.example.application.port.in.CreateAccountUseCase;
import org.example.application.port.in.GetAccountBalanceQuery;
import org.example.application.port.in.ListAccountQuery;
import org.example.application.port.in.LoadAccountQuery;
import org.example.application.port.in.SendMoneyUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AccountController {
	private final GetAccountBalanceQuery getAccountBalanceQuery;
	private final ListAccountQuery listAccountQuery;
	private final LoadAccountQuery loadAccountQuery;

	private final SendMoneyUseCase sendMoneyUseCase;
	private final CreateAccountUseCase createAccountUseCase;

	@GetMapping("/accounts")
	public List<AccountResource> listAccounts() {
		// todo
		return null;
	}

	@GetMapping("/accounts/{accountId}")
	public AccountResource getAccount(@PathVariable("accountId") Long accountId) {
		// todo
		return null;
	}

	@GetMapping("/accounts/{accountId}/balance")
	public long getAccountBalance(@PathVariable("accountId") Long accountId) {
		// todo
		return 0;
	}

	@PostMapping("/accounts")
	public AccountResource createAccount(@RequestBody AccountResource account) {
		// todo
		return null;
	}

	@PostMapping("/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}")
	public void sendMoney(
		@PathVariable("sourceAccountId") Long sourceAccountId,
		@PathVariable("targetAccountId") Long targetAccountId,
		@PathVariable("amount") Long amount) {
		// todo
	}
}
