package io.hashimati.offerservice.services;

import java.util.Date;

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
import io.reactivex.Flowable;
import io.reactivex.Single;

@Singleton
public class OfferServices {


    @Inject 
     RequestsClient requestsClient; 


    private final MongoClient mongoClient;
    public OfferServices(MongoClient mongoClient)
    {
        this.mongoClient = mongoClient;
    }

    private MongoCollection<Offer> getCollection() {

    
            return mongoClient
                .getDatabase("requestsDB")
                .getCollection("offers", Offer.class);
    }
    private Single<Offer> findAsSingle(BsonDocument query)
    {
        return Single
        .fromPublisher(getCollection()
        .find(query)); 
    }

    private Flowable<Offer> findAsFlowable(BsonDocument query)
    {
        return Flowable
        .fromPublisher(getCollection()
        .find(query)); 
    }
    public Single<Offer> save(Offer offer, String token){
        
        Request request = requestsClient.findRequestByNo(offer.getRequestNumber(), token).blockingGet();
                
        if(request.getStatus() == RequestStatus.INITIATED){
        
        Long i = Single.fromPublisher(getCollection().countDocuments(new BsonDocument()
                    .append("providerName", new BsonString(offer.getProviderName()))))
                    .blockingGet();  

        offer.setId(offer.getProviderName() + "_" + (i.longValue() + 1) ); 
        offer.setRequesterName(request.getRequesterName());
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
    public Flowable<Offer> findOffersByRequestNo(String requestNumber)
    {
        return findAsFlowable(new BsonDocument().append("requestNumber", new BsonString(requestNumber))); 
    }


    public Flowable<Offer> findOffersByRequesterNoAndProviderName(String requestNumber, String username)
    {
        return findAsFlowable(new BsonDocument().append("requestNumber", new BsonString(requestNumber)
        ).append("providerName", new BsonString(username)));
    }
    public Single<Offer> findOfferByOfferNumber(String offerNumber){
        return findAsSingle(new BsonDocument().append("_id", new BsonString(offerNumber))); 
    }

    public Single<Offer> findOfferByOfferNumberandProviderName(String offerNumber, String username)
    {
        return findAsSingle(new BsonDocument().append("_id", new BsonString(offerNumber))
        .append("providerName", new BsonString(username))); 

    }
	public Single<String> takeAction(String requestId, String offerId, OfferStatus offerStatus,String username){
    
        BsonDocument filter = new BsonDocument()
        .append("_id", new BsonString(offerId)) 
        .append("requestNumber", new BsonString(requestId))
        .append("requesterName", new BsonString(username)); 


        Offer offer = findAsSingle(filter).blockingGet();

        offer.setStatus(offerStatus); 
        offer.setLastUpdate(new Date()); 

        return Single.fromPublisher(getCollection().findOneAndReplace(filter, offer))
        .map(x->"Success")
        .onErrorReturnItem("failed");  
	}
}
