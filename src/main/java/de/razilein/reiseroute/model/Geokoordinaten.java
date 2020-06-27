package de.razilein.reiseroute.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Geokoordinaten {

    private BigDecimal breitengrad;

    private BigDecimal laengengrad;

}
