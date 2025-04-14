package org.example.chap11.identitykey;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private Key key;
    private String customer;
    private List<LineItem> lineItems;

    public Order(Key key, String customer) {
        this.key = key;
        this.customer = customer;
        this.lineItems = new ArrayList<>();
    }

    public Key getKey() {
        return this.key;
    }

    public void addLineItem(LineItem lineItem) {
        this.lineItems.add(lineItem);
    }
}
