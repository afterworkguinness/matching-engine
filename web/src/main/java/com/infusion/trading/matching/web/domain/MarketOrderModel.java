package com.infusion.trading.matching.web.domain;

import com.infusion.trading.matching.domain.OrderDesignation;
import com.infusion.trading.matching.domain.OrderSide;

public class MarketOrderModel {

    private String symbol;
    private int quantity;
    private OrderSide side;
    private OrderDesignation designation;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public OrderDesignation getDesignation() {
        return this.designation;
    }

    public void setDesignation(OrderDesignation designation) {
        this.designation = designation;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public  void setSide(OrderSide side) {
        this.side = side;
    }

    public OrderSide getSide() {
        return this.side;
    }
}
