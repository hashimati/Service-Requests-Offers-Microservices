package io.hashimati.requestservice.domains;

import java.util.Date;
import io.hashimati.requestservice.domains.enums.RequestStatus;
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
public class Request {
    private String id, type, title, detail, requesterName, city;

    private RequestStatus status = RequestStatus.INITIATED;
    private Date date , lastUpdate = date = new Date();

    private Location location; 
    
}
