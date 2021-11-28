package com.enhanced.sustenance.ui.pantry_fragment;

public class FoodItemCardTemplate {
    private String name;
    private String qty;

    public FoodItemCardTemplate(String name, String qty) {
        this.name = name;
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public String getQty() {
        return qty;
    }
}
