package io.hashimati.requestservice.services;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;

import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonString;
import org.bson.conversions.Bson;

import io.hashimati.requestservice.domains.Request;
import io.hashimati.requestservice.domains.enums.RequestStatus;
import io.micronaut.runtime.event.annotation.EventListener;
import io.reactivex.Flowable;
import io.reactivex.Single;


@Singleton
public class RequestServices {

    private final MongoClient mongoClient;
    

    public RequestServices(MongoClient mongoClient)
    {
        this.mongoClient = mongoClient;
    }


    public Single<Request> save(Request request){

        Long x = Single.fromPublisher(getCollection()
                .countDocuments(new BsonDocument()
                        .append("requesterName", new BsonString(request.getRequesterName()))))
                        .blockingGet();

        request.setId(request.getRequesterName() + "_" + x.longValue());
        request.setStatus(RequestStatus.INITIATED);

        return   Single.fromPublisher(getCollection().insertOne(request))
                .map(success->request);

    }

    private MongoCollection<Request> getCollection() {
            return mongoClient
                .getDatabase("requestsDB")
                .getCollection("requests", Request.class);
    }

 


    
    public Single<Request> findRequestByNo(String requestNo) {
        
        return Single.fromPublisher(getCollection().find(new BsonDocument().append("_id", new BsonString(requestNo)))); 
        
    }
    
    public Flowable<Request> findAll()
    {
            return Flowable.fromPublisher(getCollection().find(new BsonDocument().append("status", new BsonString(RequestStatus.INITIATED.toString())))); 

    }

    public Flowable<Request> findAll(String username){
        return Flowable.fromPublisher(getCollection().find(new BsonDocument().append("requesterName", new BsonString(username)))); 
    }

	public Single<String> takeAction(String requestId, RequestStatus done){
        BsonDocument filter = new BsonDocument().append("_id", new BsonString(requestId)); 

        Request request = Single.fromPublisher(getCollection().find(filter).limit(1).first()).blockingGet(); 
        request.setStatus(done);

        return Single.fromPublisher(getCollection().findOneAndReplace(filter, request))
        .map(success->"success")
        .onErrorReturnItem("failed"); 
        


	}
	public Flowable<Request> findOpenRequests() {
		return null;
	}
	public Flowable<Request> findByCity(String city) {
        return Flowable.fromPublisher(getCollection().find(new BsonDocument().append("city", new BsonString(city)))); 
    }


	public Flowable<Request> findNearBy(HashMap<String, Double> location) {

        // get Locations; 

        BsonArray coordinates = new BsonArray(); 
        try{
            
            coordinates.add(new BsonDouble(location.get("longitude"))); 
            coordinates.add(new BsonDouble(location.get("latitude")));
            


            return Flowable.fromPublisher(getCollection().find(new BsonDocument()
            .append("location", new BsonDocument()
            .append("$near", new BsonDocument()
            .append("$geometry", new BsonDocument()
            .append("type", new BsonString("Point")) .append("coordinates", coordinates))
            .append("$minDistance", new BsonDouble(0))
            .append("$maxDistance", new BsonDouble(100)))))) ;
        }
        catch(Exception ex)
        {
            return Flowable.just(null); 
        }
    }


	
}
