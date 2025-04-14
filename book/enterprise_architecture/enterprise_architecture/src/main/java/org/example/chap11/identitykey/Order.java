package org.example.chap11.identitykey;

public class Order {
    private Key key;
    private String customer;

    public Order(Key key, String customer) {
        this.key = key;
        this.customer = customer;
    }

    public Key getKey() {
        return this.key;
    }
}
