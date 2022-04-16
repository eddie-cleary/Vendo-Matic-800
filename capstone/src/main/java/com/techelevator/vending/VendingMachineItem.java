package com.techelevator.vending;

import com.techelevator.items.Consumable;

import java.math.BigDecimal;

public class VendingMachineItem {

    private String name;
    private BigDecimal price;
    private Consumable type;
    private String id;
    private static final int DEFAULT_QUANTITY = 5;
    private int quantitySold;

    private int quantity;

    public VendingMachineItem(String id, String name, BigDecimal price, Consumable type) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.id = id;
        this.quantity = DEFAULT_QUANTITY;
        this.quantitySold = 0;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Consumable getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String vend() {
        this.quantity -= 1;
        return this.type.makeSound();
    }



}