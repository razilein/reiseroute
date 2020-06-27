package de.razilein.reiseroute.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Adresse {
    
    private Integer nummer;
    
    private String standort;
    
    private String strasse;
    
    private String hausnummer;
    
    private String plz;
    
    private String ort;
    
    private Geokoordinaten koordinaten;

}
