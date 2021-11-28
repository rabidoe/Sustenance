package com.enhanced.sustenance.ui.shopping_fragment;

import com.enhanced.sustenance.utils.Enums;

public class ShoppingItemObject {
    private String name;
    private String qty;
    private Enums.UNITS unit;
    private Enums.CATEGORIES category;
    private String aisle;
    private String checked;

    public ShoppingItemObject(String name) {
        this.name = name;
    }

    public ShoppingItemObject(String name, String qty, Enums.UNITS unit, Enums.CATEGORIES category, String aisle) {
        this.name = name;
        this.qty = qty;
        this.unit = unit;
        this.category = category;
        this.aisle = aisle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public Enums.UNITS getUnit() {
        return unit;
    }

    public void setUnit(Enums.UNITS unit) {
        this.unit = unit;
    }

    public Enums.CATEGORIES getCategory() {
        return category;
    }

    public void setCategory(Enums.CATEGORIES category) {
        this.category = category;
    }

    public String getAisle() {
        return aisle;
    }

    public void setAisle(String aisle) {
        this.aisle = aisle;
    }

    public boolean isChecked() {
        return checked != null && checked.equalsIgnoreCase("true");
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        if (checked) {
            this.checked = "true";
        } else {
            this.checked = "false";
        }
    }

}
