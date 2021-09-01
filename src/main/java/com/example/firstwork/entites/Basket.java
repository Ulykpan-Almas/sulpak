package com.example.firstwork.entites;

public class Basket {

    private Items item;

    private int amount;

    public Basket() {}

    public Basket(Items item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    public Items getItem() {
        return item;
    }

    public void setItem(Items item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
