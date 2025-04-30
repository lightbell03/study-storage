package org.example.domain;

public class Money {
	private long amount;

	private Money(long amount) {
		this.amount = amount;
	}
	public static Money of(long amount) {
		return new Money(amount);
	}
}
