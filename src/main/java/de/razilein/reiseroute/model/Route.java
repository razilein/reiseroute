package de.razilein.reiseroute.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Route {

    private List<Adresse> adressen;

    private BigDecimal distanz;

    public void addDistanz(final BigDecimal distanz) {
        this.distanz = this.distanz.add(distanz);
    }

}
