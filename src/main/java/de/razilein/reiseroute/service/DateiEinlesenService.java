package de.razilein.reiseroute.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import de.razilein.reiseroute.model.Adresse;
import de.razilein.reiseroute.model.Geokoordinaten;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DateiEinlesenService {

    @Value("${file.encoding}")
    public String charset;

    private final ResourceLoader resourceLoader;

    /**
     * Liest die im Klassenpfad befindliche Datei "msg_standorte_deutschland.csv" zeilenweise mit dem angegebenen Encoding ein. Die erste
     * Zeile (Spaltenbeschriftung) wird dabei übersprungen. Die eingelesenen Zeilen müssen eine Länge von 8 Zeichen aufweisen. Zeilen die
     * nicht der Länge von 8 Zeichen entsprechen werden ignoriert. Die gültigen Zeilen werden in
     * {@link de.razilein.reiseroute.model.Adresse} umgewandelt und zurückgegeben.<br>
     * Die Datei aus dem Klassenpfad muss dazu ins Temp-Verzeichnis kopiert werden, da diese in der JAR-Datei sonst nicht erreichbar ist.
     *
     * @return
     * @throws IOException
     */
    public List<Adresse> leseAdressen() throws IOException {
        final File file = new File(FileUtils.getTempDirectory(), "msg_standorte_deutschland.csv");
        final InputStream is = resourceLoader.getResource("classpath:msg_standorte_deutschland.csv").getInputStream();
        FileUtils.copyInputStreamToFile(is, file);

        return FileUtils.readLines(file, charset).stream().skip(1).map(line -> StringUtils.split(line, ","))
                .filter(DateiEinlesenService::hasValidLength).map(DateiEinlesenService::createAdresse).collect(Collectors.toList());
    }

    private static boolean hasValidLength(final String[] lineparts) {
        return lineparts != null && lineparts.length == 8;
    }

    private static Adresse createAdresse(final String[] lineparts) {
        final Geokoordinaten geo = new Geokoordinaten(new BigDecimal(lineparts[6]), new BigDecimal(lineparts[7]));
        return new Adresse(Integer.valueOf(lineparts[0]), lineparts[1], lineparts[2], lineparts[3], lineparts[4], lineparts[5], geo);
    }

}
