package org.example.chap11.identitykey;

public class LineItem {
    private Key key;
    private Integer amount;
    private String product;

    public LineItem(Key key, Integer amount, String product) {
        this.key = key;
        this.amount = amount;
        this.product = product;
    }
}
