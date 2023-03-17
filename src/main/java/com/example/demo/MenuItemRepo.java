package com.example.demo;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import javax.swing.text.Document;
import java.util.List;

public interface MenuItemRepo extends MongoRepository<FastFoodMenuItem, String> {

    FastFoodMenuItem deleteAllByName(String name);

    List<FastFoodMenuItem> findAllFastFoodMenuItemByIngredientsIsContaining(String ingredient);

    @Aggregation("{ '$project':  {'_id':  '$name', 'type':  '$type'}}")
    List<String> findAllTypes();

    @Aggregation("{ '$project':  {'_id':  '$name'}}")
    List<String> findAllNames();

}
