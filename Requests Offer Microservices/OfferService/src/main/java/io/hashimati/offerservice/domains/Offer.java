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
    private String id, providerName, message, requestNumber, requesterName;
    private double price; 
    private OfferStatus status = OfferStatus.SENT;
    private Date 
    date , lastUpdate = date = new Date();
}