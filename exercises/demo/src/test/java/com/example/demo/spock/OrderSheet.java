package com.example.demo.spock;

public class OrderSheet {
    private long orderAmount;

    public OrderSheet(long orderSheet) {
        this.orderAmount = orderSheet;
    }

    public long getTotalOrderAmount() {
        return orderAmount;
    }
}
