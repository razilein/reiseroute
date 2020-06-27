package de.razilein.reiseroute.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import de.razilein.reiseroute.model.Geokoordinaten;

class BerechneDistanzUtilsTest {

    @Test
    void testGetDistanz() {
        final Geokoordinaten geo1 = new Geokoordinaten(new BigDecimal("48.229035"), new BigDecimal("11.686153"));
        final Geokoordinaten geo2 = new Geokoordinaten(new BigDecimal("48.126258"), new BigDecimal("8.325873"));
        BigDecimal distanz = BerechneDistanzUtils.getDistanz(geo1, geo2);
        assertThat(distanz).isEqualTo(new BigDecimal("249.4"));

        distanz = BerechneDistanzUtils.getDistanz(geo1, geo1);
        assertThat(distanz).isEqualTo(new BigDecimal("0.000"));
    }

}
