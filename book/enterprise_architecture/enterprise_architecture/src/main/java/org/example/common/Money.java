package org.example.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Money {
    private BigDecimal amount;

    private Money(BigDecimal amount) {
        this.amount = amount;
    }

    public static Money dollars(BigDecimal amount) {
        return new Money(amount);
    }

    public static Money dollars(int amount) {
        return new Money(new BigDecimal(amount));
    }

    public Money add(Money money) {
        return new Money(amount.add(money.amount));
    }

    public BigDecimal getAmount() {
        return new BigDecimal(amount.toString());
    }

    public Money[] allocate(int divide) {
        BigDecimal divided = amount.divide(new BigDecimal(divide), RoundingMode.HALF_EVEN);
        Money[] moneys = new Money[divide];
        for (int i = 0; i < divide; i++) {
            moneys[i] = new Money(divided);
        }

        return moneys;
    }
}
