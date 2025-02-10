package org.example.domainmodel.domainmodel;

import org.example.common.MfDate;
import org.example.common.Money;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Contract {
    private Product product;
    private Money revenue;
    private MfDate whenSigned;
    private Long id;
    private List<RevenueRecognition> revenueRecognitions = new ArrayList<>();

    public Contract(Product product, Money revenue, MfDate whenSigned) {
        this.product = product;
        this.revenue = revenue;
        this.whenSigned = whenSigned;
    }

    public Money getRevenue() {
        return this.revenue;
    }

    public Product getProduct() {
        return this.product;
    }

    public MfDate getWhenSigned() {
        return this.whenSigned;
    }

    public Money recognitionRevenue(MfDate asOf) {
        Money result = Money.dollars(0);
        Iterator<RevenueRecognition> it = revenueRecognitions.iterator();
        while(it.hasNext()) {
            RevenueRecognition r = it.next();
            if(r.isRecognizableBy(asOf)) {
                result = result.add(r.getAmount());
            }
        }

        return result;
    }

    public void addRevenueRecognition(RevenueRecognition revenueRecognition) {
        revenueRecognitions.add(revenueRecognition);
    }
}
