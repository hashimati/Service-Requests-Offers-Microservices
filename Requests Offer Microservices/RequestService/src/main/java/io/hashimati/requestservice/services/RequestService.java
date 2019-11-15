package io.hashimati.requestservice.services;

import java.util.Date;
import java.util.HashMap;

import javax.inject.Singleton;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;

import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonString;

import io.hashimati.requestservice.domains.Request;
import io.hashimati.requestservice.domains.enums.RequestStatus;
import io.reactivex.Flowable;
import io.reactivex.Single;


@Singleton
public class RequestService {

    private final MongoClient mongoClient;
    

    public RequestService(MongoClient mongoClient)
    {
        this.mongoClient = mongoClient;
    }


    private MongoCollection<Request> getCollection() {
            return mongoClient
                .getDatabase("requestsDB")
                .getCollection("requests", Request.class);
    }

    private Single<Request> findAsSingle(BsonDocument query)
    {

        return Single.fromPublisher(getCollection().find(query)); 
    }
    
    private Flowable<Request> findAsFlowable(BsonDocument query)
    {

        return Flowable.fromPublisher(getCollection().find(query)); 
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
    
    public Single<Request> findRequestByNo(String requestNo) {
        
        return findAsSingle(new BsonDocument().append("_id", new BsonString(requestNo))); 
        
    }
    
    public Flowable<Request> findAll()
    {
            BsonDocument query = new BsonDocument().append("status", new BsonString(RequestStatus.INITIATED.toString())); 
            return findAsFlowable(query); 

    }
    
    public Flowable<Request> findAll(String username){
        return findAsFlowable(new BsonDocument()
                                .append("requesterName", new BsonString(username))); 
    }
    public Flowable<Request> findByCity(String city) {
        BsonDocument query = new BsonDocument()
        .append("status", new BsonString(RequestStatus.INITIATED.toString()))
        .append("city", new BsonString(city)); 

        return findAsFlowable(query); 
    }


	public Flowable<Request> findNearBy(HashMap<String, Double> location) {

         
        try{
            // get Locations; 
            BsonArray coordinates = new BsonArray();    
            coordinates.add(new BsonDouble(location.get("longitude"))); 
            coordinates.add(new BsonDouble(location.get("latitude")));

            BsonDocument query = new BsonDocument()
            .append("status", new BsonString(RequestStatus.INITIATED.toString()))
            .append("location", new BsonDocument()
            .append("$near", new BsonDocument()
            .append("$geometry", new BsonDocument()
            .append("type", new BsonString("Point")) .append("coordinates", coordinates))
            .append("$minDistance", new BsonDouble(0))
            .append("$maxDistance", new BsonDouble(100)))); 

            return findAsFlowable(query) ;
        }
        catch(Exception ex)
        {
            return Flowable.just(null); 
        }
    }


	public Single<String> takeAction(String requestId, RequestStatus done){
        BsonDocument filter = new BsonDocument().append("_id", new BsonString(requestId)); 

        Request request = findAsSingle(filter).blockingGet(); 
        //Single.fromPublisher(getCollection().find(filter).limit(1).first()).blockingGet(); 
        request.setStatus(done);
        request.setLastUpdate(new Date());



        return Single.fromPublisher(getCollection().findOneAndReplace(filter, request))
        .map(success->"success")
        .onErrorReturnItem("failed"); 
        


	}
	
		
}
