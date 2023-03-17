package com.example.demo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

enum TYPE {
    VEGAN,
    VEGETARIAN,
    REGULAR,
    NON_DAIRY,
    GLUTEN_FREE
}
@Data
@Document
public class CookBookItem {
    @Id
    private String id;
    @Indexed(unique = true)
    private String name;

    private TYPE type;
    private List<String> ingredients;
    private String instructions;

    public CookBookItem(String name, List<String> ingredients, String instructions, TYPE type) {
        super();
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.type = type;
    }

}
