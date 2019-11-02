package io.hashimati.offerservice.domains;

import io.hashimati.offerservice.domains.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Request {
    private String id, type, title, detail, requesterName, city;

    private RequestStatus status = RequestStatus.INITIATED;
    private Date date , lastUpdate = date = new Date();

    private Location location; 
    
}