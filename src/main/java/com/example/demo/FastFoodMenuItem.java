package com.example.demo;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class FastFoodMenuItem extends CookBookItem {
    private String restaurant;
    public FastFoodMenuItem(String name, List<String> ingredients, String instructions, TYPE type, String restaurant){
        super(name,ingredients,instructions,type);
        this.restaurant = restaurant;
    }
}
