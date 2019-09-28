package io.hashimati.offerservice.services;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;

import io.hashimati.offerservice.clients.RequestsClient;
import io.hashimati.offerservice.domains.Offer;
import io.hashimati.offerservice.domains.Request;
import io.hashimati.offerservice.domains.enums.OfferStatus;
import io.hashimati.offerservice.domains.enums.RequestStatus;
import io.reactivex.Single;

import org.bson.BsonDocument;
import org.bson.BsonString;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OfferServices {


    @Inject 
     RequestsClient requestsClient; 


    private final MongoClient mongoClient;
    public OfferServices(MongoClient mongoClient)
    {
        this.mongoClient = mongoClient;
    }


    public Single<Offer> save(Offer offer){
        
        Request request = requestsClient.findRequestByNo(offer.getOrderNumber()); 

        System.out.println(request);
        
        if(request.getStatus() == RequestStatus.INITIATED){
        
        Long i = Single.fromPublisher(getCollection().countDocuments(new BsonDocument()
                    .append("orderNumber", new BsonString(offer.getOrderNumber()))))
                    .blockingGet();  

        offer.setOfferID(offer.getOrderNumber() + "_" + (i.longValue() + 1) ); 
        return   Single.fromPublisher(getCollection().insertOne(offer))
                .map(success->offer);
        }
        else{
        
            offer.setStatus(OfferStatus.REJECTED);
            return Single
                .fromPublisher(null)
                .map(success->offer); 
        }

    }
    private MongoCollection<Offer> getCollection() {

            return mongoClient
                .getDatabase("requestsDB")
                .getCollection("offers", Offer.class);
    }

}
