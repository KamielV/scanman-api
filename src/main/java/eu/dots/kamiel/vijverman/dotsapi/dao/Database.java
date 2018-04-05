/*
 */
package eu.dots.kamiel.vijverman.dotsapi.dao;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import java.util.Arrays;
import com.mongodb.Block;

import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author kamie_itw2x3t
 */
public class Database {
    private static MongoDatabase getDB() {
    MongoClientURI connectionString = new MongoClientURI("mongodb://heroku_4t1w3kf2:ra7843eppuooi5jkbqgh7an4g0@ds019143.mlab.com:19143/heroku_4t1w3kf2?authSource=heroku_4t1w3kf2");
    MongoClient mongoClient = new MongoClient(connectionString);
    MongoDatabase database = mongoClient.getDatabase("heroku_4t1w3kf2");
    return database;
    }
    
    public static MongoCollection<Document> getCollection(String collection) {
        MongoDatabase database = Database.getDB();
        MongoCollection<Document> col = database.getCollection(collection);
        return col;
    }
    
}
