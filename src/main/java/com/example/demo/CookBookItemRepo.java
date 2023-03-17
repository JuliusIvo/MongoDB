package com.example.demo;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Collection;
import java.util.List;

public interface CookBookItemRepo extends MongoRepository<CookBookItem, String> {

    @Query("{name: '?0'}")
    CookBookItem findCookBookItemByName(String name);

    @Query("{instructions: '?0'}")
    CookBookItem findCookBookItemByInstructions(String instructions);

    @Query("{instructions:  '?0'}")
    List<CookBookItem> findAllCookBookItemByInstructions(String instructions);

    List<CookBookItem> findAllCookBookItemByIngredientsIsContaining(String ingredient);

    List<CookBookItem> findAllCookBookItemByType(String type);

    CookBookItem deleteAllCookBookItemByName(String name);

    @Aggregation("{ $group: {_id: $instructions, type : {$addToSet:  $name}}}")
    List<CookBookItem> groupByInstructionsAndType();

    @Aggregation("{ '$project':  {'_id':  '$name', 'type':  '$type'}}")
    List<String> findAllTypes();

    @Aggregation("{ '$project': {'_id': '$name'}}")
    List<String> findAllNames();


    @Query("([{$lookup: {from: 'fastFoodMenuItem', let: {name: '$name', $pipeline:[{$match: {$expr:  {$eq: ['$name', '$$name']}}}], as: 'allNames'}}}]")
    Collection something();
}
