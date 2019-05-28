package com.example.bullet_journal.model;

public class NavItem {

    private int item_icon;

    private String name_of_item;

    public NavItem(){
    }

    public NavItem(int item_icon, String name_of_item){
        this.item_icon= item_icon;
        this.name_of_item= name_of_item;
    }

    public int getItem_icon() {
        return item_icon;
    }

    public void setItem_icon(int item_icon) {
        this.item_icon = item_icon;
    }

    public String getName_of_item() {
        return name_of_item;
    }

    public void setName_of_item(String name_of_item) {
        this.name_of_item = name_of_item;
    }
}
