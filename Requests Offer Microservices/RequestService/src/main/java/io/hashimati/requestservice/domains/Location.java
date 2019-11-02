package io.hashimati.requestservice.domains;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString; 

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Location {

    private String type = "Point"; 
    
    private ArrayList<Double> coordinates = new ArrayList<Double>(); 

}
