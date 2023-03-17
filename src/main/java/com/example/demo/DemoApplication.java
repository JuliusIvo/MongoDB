package com.example.demo;

import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.MapReduceAction;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Arrays;
import java.util.List;


@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		System.out.println("---------------AGGREGATION----------------");

		MongoClient mongoClient = MongoClients.create("mongodb://rootuser:rootpass@localhost:27017/admin");
		MongoDatabase database = mongoClient.getDatabase("test");
		MongoCollection<org.bson.Document> collection = database.getCollection("cookBookItem");
		collection.aggregate(
				Arrays.asList(
						Aggregates.match(Filters.eq( "instructions", "biorger")),
						Aggregates.lookup("fastFoodMenuItem", "instructions", "instructions", "burgers")
				)
		).forEach(System.out::println);

		System.out.println("-----------------MAPREDUCE---------------------");

		String map = "function() {if(this.instructions==='biorger'){emit(this, 1);}}";
		String reduce = "function(key, value) {return key.name + ' ' + key.instructions + ' ' +  key.type + ' ' + key._class;}";
		MapReduceIterable mapReduceIterable = mongoClient.getDatabase("test").getCollection("cookBookItem")
				.mapReduce(map, reduce)
				.jsMode(true)
				.action(MapReduceAction.REPLACE)
				.databaseName("test")
				.collectionName("AllItems")
				.sharded(true);
		MapReduceIterable mapReduceIterable1 = mongoClient.getDatabase("test").getCollection("fastFoodMenuItem")
				.mapReduce(map, reduce)
				.action(MapReduceAction.MERGE)
				.databaseName("test")
				.collectionName("AllItems")
				.sharded(true);
		for (Object o: mapReduceIterable) {
			System.out.println(o);
		}
		for(Object o: mapReduceIterable1){
			System.out.println(o);
		}

	}

	@Bean
	CommandLineRunner runner(CookBookItemRepo repository, CookBookItemRepo cookBookItemRepo,
							 MenuItemRepo menuRepository, MongoTemplate mongoTemplate){
		return args -> {
			CookBookItem cookBookItem = new CookBookItem("cheese burger", List.of("cheese",
					"hamburger"),
			 "biorger", TYPE.REGULAR);
			FastFoodMenuItem menuItem = new FastFoodMenuItem("another burger", List.of("ground beef", "salt", "pepper",
					"pickles", "burger buns", "ketchup"), "biorger",
					TYPE.REGULAR, "McDonalds");
		};

	}


}

