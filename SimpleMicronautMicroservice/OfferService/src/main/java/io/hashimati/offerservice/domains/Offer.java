package io.hashimati.offerservice.domains;

import java.util.Date;

import io.hashimati.offerservice.domains.enums.OfferStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Offer {
    private String id, by, message, orderNumber;
    private OfferStatus status = OfferStatus.SENT;
    private Date date , lastUpdate = date = new Date();

    // public Offer() {

    // }

    // public Date getLastUpdate() {
    //     return lastUpdate;
    // }

    // public void setLastUpdate(Date lastUpdate) {
    //     this.lastUpdate = lastUpdate;
    // }

    // public Date getDate() {
    //     return date;
    // }

    // public void setDate(Date date) {
    //     this.date = date;
    // }

    // public Offer(String offerID, String by, String message, String orderNumber, OfferStatus status) {
    
    //     this.id = offerID;
    //     this.by = by;
    //     this.message = message;
    //     this.orderNumber = orderNumber;
    //     this.status =  status;
    // }

    // public String getId() {
        
    //     return this.id; 
    // }

    // public void setId(String offerID) {
    //     this.id = offerID;
    // }

    // public String getBy() {
    //     return by;
    // }

    // public void setBy(String by) {
    //     this.by = by;
    // }

    // public String getMessage() {
    //     return message;
    // }

    // public void setMessage(String message) {
    //     this.message = message;
    // }

    // public String getOrderNumber() {
    //     return orderNumber;
    // }

    // public void setOrderNumber(String orderNumber) {
    //     this.orderNumber = orderNumber;
    // }

    // public OfferStatus getStatus() {
    //     return status;
    // }

    // public void setStatus(OfferStatus status) {
    //     this.status = status;
    // }
}

