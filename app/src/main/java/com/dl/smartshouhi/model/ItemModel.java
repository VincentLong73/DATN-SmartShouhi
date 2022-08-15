package com.dl.smartshouhi.model;

public class ItemModel {

    private String item_name;
    private String cost_item;

    public ItemModel(String item_name, String cost_item) {
        this.item_name = item_name;
        this.cost_item = cost_item;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getCost_item() {
        return cost_item;
    }

    public void setCost_item(String cost_item) {
        this.cost_item = cost_item;
    }
}
