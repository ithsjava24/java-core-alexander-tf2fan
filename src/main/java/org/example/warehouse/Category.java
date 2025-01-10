package org.example.warehouse;

public class Category {
    private String name;

    public Category(String name) {
    }

    public String getName(){
        return name;
    }

    public void setName(String newName) {
        this.name = name;
    }

    public static Category of(String name) {
        return new Category(name);
    }

}

