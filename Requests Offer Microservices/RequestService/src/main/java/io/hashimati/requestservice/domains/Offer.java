package io.hashimati.requestservice.domains;

import java.util.Date;

import io.hashimati.requestservice.domains.enums.OfferStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;



@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Data
public class Offer {
    private String id, providerName, message, requestNumber, requesterName;
    private double price; 
    private OfferStatus status = OfferStatus.SENT;
    private Date date , lastUpdate = date = new Date();
}

