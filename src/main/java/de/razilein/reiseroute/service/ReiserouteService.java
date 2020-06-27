package de.razilein.reiseroute.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.razilein.reiseroute.model.Adresse;
import de.razilein.reiseroute.model.Route;
import de.razilein.reiseroute.util.BerechneDistanzUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReiserouteService {

    @Value("${startpunkt}")
    private Integer startpunkt;

    /**
     * Berechnet die effizenteste Route aus einer Liste von Adressen mit Geokoordinaten.
     *
     * @param adressen
     *            Liste von Adressen zu denen die effizenteste Route bestimmt werden soll
     *
     * @return Effizenteste Route
     */
    public Route berechneEffizientesteRoute(final List<Adresse> adressen) {
        final List<Adresse> routen = new ArrayList<>();
        routen.add(getStartAdresse(adressen));
        final Route route = new Route(routen, BigDecimal.ZERO);
        berechneRoute(adressen, route);
        return route;
    }

    /**
     * Filtert aus der Liste der Adressen alle Adressen, die noch nicht in der Route vorhanden sind. Die Distanz vom aktuellen Standort in
     * der Route wird mit allen noch möglichen Zieladressen verglichen. Die Adressen mit der jeweils geringsten Distant wird als nächstes
     * Ziel verwendet.
     *
     * @param adressen
     * @param routen
     */
    private void berechneRoute(final List<Adresse> adressen, final Route route) {
        final List<Adresse> nochNichtBesuchteAdressen = adressen.stream().filter(a -> !route.getAdressen().contains(a))
                .collect(Collectors.toList());
        final Adresse aktuelleAdresse = route.getAdressen().get(route.getAdressen().size() - 1);

        if (nochNichtBesuchteAdressen.isEmpty()) {
            final Adresse startAdresse = getStartAdresse(adressen);
            route.getAdressen().add(startAdresse);

            final BigDecimal distanz = BerechneDistanzUtils.getDistanz(aktuelleAdresse.getKoordinaten(), startAdresse.getKoordinaten());
            route.addDistanz(distanz);
            logDistanz(aktuelleAdresse, startAdresse, distanz);
        } else {
            route.getAdressen().add(findeNaechstenOrt(route, nochNichtBesuchteAdressen, aktuelleAdresse));
            berechneRoute(adressen, route);
        }
    }

    private void logDistanz(final Adresse aktuelleAdresse, final Adresse naechsteAdresse, final BigDecimal distanz) {
        log.info("Adresse {} ({}) hat die gerinste Distanz ({} km) zur Adresse {} ({})", aktuelleAdresse.getStandort(),
                aktuelleAdresse.getNummer(), distanz, naechsteAdresse.getStandort(), naechsteAdresse.getNummer());
    }

    /**
     * Sucht die Adresse, die den geringsten Abstand zu der aktuellen Adresse hat. Dabei werden die Distanzen der aktuellen Adresse mit
     * allen Distanzen zu den noch nicht besuchten Adressen verglichen.
     *
     * @param route
     *
     * @param nochNichtBesuchteAdressen
     * @param aktuelleAdresse
     * @return Adresse die am nächsten ausgehend von der aktuellen Adresse liegt.
     */
    private Adresse findeNaechstenOrt(final Route route, final List<Adresse> nochNichtBesuchteAdressen, final Adresse aktuelleAdresse) {
        final Map<Integer, BigDecimal> distanzJeZielAdresse = nochNichtBesuchteAdressen.stream().collect(Collectors
                .toMap(Adresse::getNummer, a -> BerechneDistanzUtils.getDistanz(aktuelleAdresse.getKoordinaten(), a.getKoordinaten())));
        final Entry<Integer, BigDecimal> geringsteDistanz = distanzJeZielAdresse.entrySet().stream()
                .min((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                .orElseThrow(() -> new IllegalStateException("Distanz zwischen zwei Adressen konnte nicht ermittelt werden."));

        final Integer nummerNaechstePosition = geringsteDistanz.getKey();
        final Adresse naechsteAdresse = nochNichtBesuchteAdressen.stream().filter(a -> a.getNummer().equals(nummerNaechstePosition))
                .findFirst().orElseThrow(
                        () -> new IllegalStateException("Adresse mit Nummer " + nummerNaechstePosition + " konnte nicht gefunden werden."));
        logDistanz(aktuelleAdresse, naechsteAdresse, geringsteDistanz.getValue());
        route.addDistanz(geringsteDistanz.getValue());
        return naechsteAdresse;
    }

    /**
     * Ermittelt die Start- und Zieladresse anhand der Einstellungen in der application.properties.
     *
     * @param adressen
     * @return Start- und Zieladresse
     */
    private Adresse getStartAdresse(final List<Adresse> adressen) {
        return adressen.stream().filter(a -> a.getNummer().equals(startpunkt)).findFirst().orElseThrow(
                () -> new IllegalArgumentException("Startadresse mit Nummer " + startpunkt + " konnte nicht gefunden werden."));
    }

}
