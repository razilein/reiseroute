package de.razilein.reiseroute.util;

import java.io.IOException;
import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import de.razilein.reiseroute.model.Geokoordinaten;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class BerechneDistanzUtils {

    /**
     * Berechnet die Distanz zwischen zwei Geokoordinaten anhand der Längen- und Breitengrade mit Hilfe der Seite
     * https://www.movable-type.co.uk/scripts/latlong.html
     *
     * @return Distanz in Kilometern
     */
    public static BigDecimal getDistanz(final Geokoordinaten geo1, final Geokoordinaten geo2) {
        try (final WebClient webClient = new WebClient()) {
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setThrowExceptionOnScriptError(false);

            final HtmlPage page = webClient.getPage("https://www.movable-type.co.uk/scripts/latlong.html");

            setBreitengrad1(page, geo1.getBreitengrad());
            setLaengengrad1(page, geo1.getLaengengrad());
            setBreitengrad2(page, geo2.getBreitengrad());
            setLaengengrad2(page, geo2.getLaengengrad());

            return getDistanz(page);
        } catch (FailingHttpStatusCodeException | IOException e) {
            throw new IllegalStateException("Fehler beim Laden der Seite zum Berechnen der Distanz", e);
        }
    }

    private static BigDecimal getDistanz(final HtmlPage page) {
        try {
            // Auf Link klicken um die Berechnung zu triggern.
            page.getAnchorByText("see it on a map").click();
        } catch (final IOException e) {
            throw new IllegalStateException("Konnte nicht auf Link 'see it on a map' klicken", e);
        }

        final HtmlElement distanzFeld = (HtmlElement) page.getByXPath("//output[contains(@class, 'result-dist')]").stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("output-Feld mit class result-dist nicht auffindbar."));
        final String distanzText = StringUtils.trim(StringUtils.remove(distanzFeld.getTextContent(), "km"));
        return new BigDecimal(distanzText);
    }

    private static void setBreitengrad1(final HtmlPage page, final BigDecimal geo) {
        getFeldByNameAndSetText(page, "lat1", geo.toString());
    }

    private static void setLaengengrad1(final HtmlPage page, final BigDecimal geo) {
        getFeldByNameAndSetText(page, "lon1", geo.toString());
    }

    private static void setBreitengrad2(final HtmlPage page, final BigDecimal geo) {
        getFeldByNameAndSetText(page, "lat2", geo.toString());
    }

    private static void setLaengengrad2(final HtmlPage page, final BigDecimal geo) {
        getFeldByNameAndSetText(page, "lon2", geo.toString());
    }

    private static void getFeldByNameAndSetText(final HtmlPage page, final String name, final String text) {
        final HtmlInput feld = getFeldByName(page, name);
        log.debug("Setze Text auf '{}'", text);
        feld.setValueAttribute(text);
        feld.setTextContent(text);
    }

    private static HtmlInput getFeldByName(final HtmlPage page, final String name) {
        log.debug("Suche Feld mit name = '{}'", name);
        return (HtmlInput) page.getElementsByName(name).stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("Feld mit Name " + name + " nicht auffindbar."));
    }

}
