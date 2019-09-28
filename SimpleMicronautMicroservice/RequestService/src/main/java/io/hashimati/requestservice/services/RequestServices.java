package io.hashimati.requestservice.services;

import javax.inject.Singleton;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import io.hashimati.requestservice.domains.Request;
import io.hashimati.requestservice.domains.enums.RequestStatus;
import io.reactivex.Single;
import org.bson.BsonDocument;
import org.bson.BsonString;


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


	public Request findRequestByNo(String requestNo) {
        
        return Single.fromPublisher(getCollection().find(new BsonDocument().append("_id", new BsonString(requestNo)))).blockingGet(); 
        
	}

}
