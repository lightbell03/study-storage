package org.example.chap11.identitykey;

import org.example.common.MultiId;

public class LineItem extends DomainObject {
    @MultiId
    private LineItemKey key;
    private Integer amount;
    private String product;

    public LineItem() {}

    public LineItem(LineItemKey key, Integer amount, String product) {
        this.key = key;
        this.amount = amount;
        this.product = product;
    }

    public LineItemKey getKey() {
        return key;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getProduct() {
        return product;
    }
}
