package de.razilein.reiseroute.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.razilein.reiseroute.model.Adresse;
import de.razilein.reiseroute.model.Geokoordinaten;
import de.razilein.reiseroute.model.Route;

@SpringBootTest(classes = { ReiserouteService.class })
@ExtendWith(SpringExtension.class)
class ReiserouteServiceTest {

    @Autowired
    private ReiserouteService reiserouteService;

    @Test
    void testBerechneEffizientesteRoute() {
        final List<Adresse> adressen = new ArrayList<>();
        adressen.add(createAdresse1());
        adressen.add(createAdresse2());
        adressen.add(createAdresse3());
        adressen.add(createAdresse4());

        final Route route = reiserouteService.berechneEffizientesteRoute(adressen);
        assertThat(route.getDistanz()).isGreaterThan(BigDecimal.ZERO);
        assertThat(route.getAdressen()).hasSize(5).first().isEqualTo(createAdresse1());
        assertThat(route.getAdressen()).isEqualTo(createAdresse1());
    }

    private Adresse createAdresse1() {
        final Geokoordinaten geo = new Geokoordinaten(new BigDecimal("48.229035"), new BigDecimal("11.686153"));
        return new Adresse(1, "Ismaning/München (Hauptsitz)", "Robert-Bürkle-Straße", "1", "85737", "Ismaning", geo);
    }

    private Adresse createAdresse2() {
        final Geokoordinaten geo = new Geokoordinaten(new BigDecimal("50.829383"), new BigDecimal("12.914737"));
        return new Adresse(5, "Chemnitz", "Zwickauer Straße", "16a", "09122", "Chemnitz", geo);
    }

    private Adresse createAdresse3() {
        final Geokoordinaten geo = new Geokoordinaten(new BigDecimal("51.145511"), new BigDecimal("14.970028"));
        return new Adresse(9, "Görlitz", "Melanchthonstraße", "19", "02826", "Görlitz", geo);
    }

    private Adresse createAdresse4() {
        final Geokoordinaten geo = new Geokoordinaten(new BigDecimal("49.429596"), new BigDecimal("11.017404"));
        return new Adresse(16, "Nürnberg", "Südwestpark", "60", "90449", "Nürnberg", geo);
    }

}
