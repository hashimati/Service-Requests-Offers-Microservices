package io.hashimati.requestservice.services;

import javax.inject.Singleton;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;

import org.bson.BsonDocument;
import org.bson.BsonString;

import io.hashimati.requestservice.domains.Request;
import io.hashimati.requestservice.domains.enums.RequestStatus;
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


	public Single<String> takeAction(String requestId, RequestStatus done){
        BsonDocument filter = new BsonDocument().append("_id", new BsonString(requestId)); 

        Request request = Single.fromPublisher(getCollection().find(filter).limit(1).first()).blockingGet(); 
        request.setStatus(done);

        return Single.fromPublisher(getCollection().findOneAndReplace(filter, request))
        .map(success->"success")
        .onErrorReturnItem("failed"); 
        


	}


	
}
