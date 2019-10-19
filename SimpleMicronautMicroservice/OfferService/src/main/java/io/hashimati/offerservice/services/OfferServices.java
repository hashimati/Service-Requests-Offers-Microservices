package io.hashimati.offerservice.services;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;

import org.bson.BsonDocument;
import org.bson.BsonString;

import io.hashimati.offerservice.clients.RequestsClient;
import io.hashimati.offerservice.domains.Offer;
import io.hashimati.offerservice.domains.Request;
import io.hashimati.offerservice.domains.enums.OfferStatus;
import io.hashimati.offerservice.domains.enums.RequestStatus;
import io.micronaut.security.utils.SecurityService;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Singleton
public class OfferServices {


    @Inject 
     RequestsClient requestsClient; 

    @Inject
    SecurityService securityService; 


    private final MongoClient mongoClient;
    public OfferServices(MongoClient mongoClient)
    {
        this.mongoClient = mongoClient;
    }


    public Single<Offer> save(Offer offer, String token){
        

        
        Single<Request> request = requestsClient.findRequestByNo(offer.getOrderNumber(), token);
        
        request.doOnError(System.out::println);
        
        if(request.blockingGet().getStatus() == RequestStatus.INITIATED){
        
        Long i = Single.fromPublisher(getCollection().countDocuments(new BsonDocument()
              //.append("orderNumber", new BsonString(offer.getOrderNumber()))
                    .append("providerName", new BsonString(offer.getProviderName()))))
                    .blockingGet();  

        offer.setId(offer.getProviderName() + "_" + (i.longValue() + 1) ); 
        return Single.fromPublisher(getCollection().insertOne(offer))
                .map(success->offer);
        }
        else{
        
            offer.setStatus(OfferStatus.REJECTED);
            return Single
                .fromPublisher(null)
                .map(success->offer); 
        }

    }
    public Flowable<Offer> findOffersByRequestNo(String requestNo)
    {
        return Flowable
        .fromPublisher(getCollection()
        .find(new BsonDocument().append("orderNumber", new BsonString(requestNo)))); 
        
    }
    private MongoCollection<Offer> getCollection() {

    
            return mongoClient
                .getDatabase("requestsDB")
                .getCollection("offers", Offer.class);
    }


	public Single<String> takeAction(String requestId, String offerId, OfferStatus offerStatus,String username){
        BsonDocument filter = new BsonDocument()
        .append("_id", new BsonString(offerId)) 
        .append("orderNumber", new BsonString(requestId))
        .append("requesterName",new BsonString(username)); 
        
        Offer offer = Single.fromPublisher(getCollection().find(
            filter
        )).blockingGet();
        
        offer.setStatus(offerStatus); 
        
        
        return Single.fromPublisher(getCollection().findOneAndReplace(filter, offer))
        .map(x->"Success")
        .onErrorReturnItem("failed");  
	}


	

}
