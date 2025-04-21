package org.example.chap11.identitykey;

import org.example.common.Column;
import org.example.common.Id;

import java.util.ArrayList;
import java.util.List;

public class Order {
    @Id
    @Column(name = "id")
    private Long id;
    private String customer;
    private List<LineItem> lineItems;

    public Order(Long id, String customer) {
        this.id = id;
        this.customer = customer;
        this.lineItems = new ArrayList<>();
    }

    public Long getId() {
        return this.id;
    }

    public String getCustomer() {
        return customer;
    }

    public void addLineItem(LineItem lineItem) {
        this.lineItems.add(lineItem);
    }
}
