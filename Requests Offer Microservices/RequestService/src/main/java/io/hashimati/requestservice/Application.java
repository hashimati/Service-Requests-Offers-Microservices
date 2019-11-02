package io.hashimati.requestservice;

import javax.inject.Inject;

import com.mongodb.reactivestreams.client.MongoClient;

import org.bson.BsonDocument;
import org.bson.BsonString;

import io.hashimati.requestservice.domains.Request;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.Micronaut;
import io.micronaut.runtime.event.annotation.EventListener;

public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }

    @Inject
    private MongoClient mongoClient; 

    @EventListener
    void init(StartupEvent startupEvent)
    {
        //create 2dsphere index to query requests by locations. 
        mongoClient
        .getDatabase("requestsDB")
        .getCollection("requests", Request.class)
        .createIndex(new BsonDocument()
        .append("location", new BsonString("2dsphere")));
    }
}