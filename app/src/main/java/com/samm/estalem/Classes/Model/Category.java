package com.samm.estalem.Classes.Model;

public class Category {
    private int id;
    private String categoryName;


    public Category( ) {
    }
    public Category(int id , String categoryName) {
        this.categoryName = categoryName;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }

}
