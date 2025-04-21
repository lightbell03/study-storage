package org.example.chap11.identitykey;

import org.example.common.Column;

public class LineItemKey {
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "id")
    private Long id;

    public LineItemKey() {}

    public LineItemKey(Long orderId, Long id) {
        this.orderId = orderId;
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getId() {
        return id;
    }
}
