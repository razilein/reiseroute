package de.razilein.reiseroute;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.razilein.reiseroute.model.Adresse;
import de.razilein.reiseroute.model.Route;
import de.razilein.reiseroute.service.DateiEinlesenService;
import de.razilein.reiseroute.service.ReiserouteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@SpringBootApplication
@Slf4j
public class ReiserouteApplication implements CommandLineRunner {

    private final DateiEinlesenService dateiEinlesenService;

    private final ReiserouteService service;

    public static void main(final String[] args) {
        SpringApplication.run(ReiserouteApplication.class, args);
    }

    @Override
    public void run(final String... args) throws Exception {
        final List<Adresse> adressen = dateiEinlesenService.leseAdressen();
        log.info("{} Adressen wurden gelesen.", adressen.size());

        final Route route = service.berechneEffizientesteRoute(adressen);
        log.info("Folgende Route mit einer Gesamtdistanz von {} km wurde ermittelt: {}", route.getDistanz(), route.getAdressen().stream()
                .map(a -> a.getNummer() + StringUtils.SPACE + a.getStandort()).collect(Collectors.joining(", ")));
    }

}
