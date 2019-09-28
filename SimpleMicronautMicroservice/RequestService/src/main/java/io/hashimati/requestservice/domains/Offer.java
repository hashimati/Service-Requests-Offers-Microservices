package io.hashimati.requestservice.domains;


import io.hashimati.requestservice.domains.enums.OfferStatus;

public class Offer
{
    private String offerID, by , message , orderNumber;
    private OfferStatus status;


    public Offer(){}
    public Offer(String offerID, String by, String message, String orderNumber, OfferStatus status) {
            
        this.offerID = offerID;
        this.by = by;
        this.message = message;
        this.orderNumber = orderNumber;
        this.status =  status;
    }

    public String getOfferID() {
        return offerID;
    }

    public void setOfferID(String offerID) {
        this.offerID = offerID;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public OfferStatus getStatus() {
        return status;
    }

    public void setStatus(OfferStatus status) {
        this.status = status;
    }
}

