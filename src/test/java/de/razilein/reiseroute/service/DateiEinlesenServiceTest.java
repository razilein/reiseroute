package de.razilein.reiseroute.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.razilein.reiseroute.model.Adresse;

@SpringBootTest(classes = { DateiEinlesenService.class })
@ExtendWith(SpringExtension.class)
class DateiEinlesenServiceTest {

    @Autowired
    private DateiEinlesenService dateiEinlesenService;

    @Test
    void testLeseAdressen() throws IOException {
        final List<Adresse> adressen = dateiEinlesenService.leseAdressen();
        assertThat(adressen).hasSize(4).first().satisfies(adresse -> {
            assertThat(adresse.getNummer()).isEqualTo(1);
            assertThat(adresse.getStandort()).isEqualTo("Ismaning/München (Hauptsitz)");
            assertThat(adresse.getStrasse()).isEqualTo("Robert-Bürkle-Straße");
            assertThat(adresse.getHausnummer()).isEqualTo("1");
            assertThat(adresse.getPlz()).isEqualTo("85737");
            assertThat(adresse.getOrt()).isEqualTo("Ismaning");
            assertThat(adresse.getKoordinaten()).isNotNull().satisfies(koordinaten -> {
                assertThat(koordinaten.getBreitengrad()).isEqualTo(new BigDecimal("48.229035"));
                assertThat(koordinaten.getLaengengrad()).isEqualTo(new BigDecimal("11.686153"));
            });
        });
    }

}
