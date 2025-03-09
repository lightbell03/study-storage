package org.example.chap10.domainmodel.domainmodel;

import org.example.common.MfDate;
import org.example.common.Money;

public class RevenueRecognition {
    private Money amount;
    private MfDate date;

    public RevenueRecognition(Money amount, MfDate date) {
        this.amount = amount;
        this.date = date;
    }

    public Money getAmount() {
        return amount;
    }

    public boolean isRecognizableBy(MfDate asOf) {
        return asOf.after(date) || asOf.equals(date);
    }
}
