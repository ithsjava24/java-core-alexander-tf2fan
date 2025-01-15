package org.example.warehouse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Category {
    private final String name;
    private static Category instance;

    private Category(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public static Category of(String name) {
        if (name == null)
            throw new IllegalArgumentException("Category name can't be null");
        if(instance == null || !instance.name.equals(name))
            instance = new Category(name.substring(0, 1).toUpperCase() + name.substring(1));
        return instance;
    }

}

