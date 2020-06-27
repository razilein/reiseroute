# Reiseroute
## Allgemein
Mit Hilfe des Programms kann anhand einer CSV-Datei, welche Standortdaten inkl. Breiten- und Längengrad enthält, die effizenteste Route
ermittelt werden.
Dabei gibt es einen Startpunkt der auch gleichzeitig als Endpunkt dient.
Jeder Standort darf nur einmalig bereist werden.

In der `src/main/resources/application.properties` kann mit Hilfe der property `startpunkt` die Nummer des Start-/Endpunktes angegeben werden.

## Begründung für den gewählten Algorithmus
Meine Idee war es, bei jedem Standort den nächst gelegenen anzufahren, bis alle Standorte bereist wurden.
Dies sollte meiner Meinung nach die effizenteste Route sein.

Ich habe mich entschieden die Berechnung der Distanz zwischen zwei Standorten mit Hilfe der Seite 
`https://www.movable-type.co.uk/scripts/latlong.html` durchzuführen, da diese bereits einen Algorithmus
bietet, um die Berechnung durchzuführen.
Diese steuere ich mit Hilfe von HtmlUnit im Hintergrund an.

Die Berechnung der Distanz selbst zu programmieren, wäre sicherlich einfacher und schneller gewesen.
Ich fand es jedoch eine interessante Idee eine andere Seite die dies bereits kann anzusteuern.

## Anleitung zum Starten der Anwendung
Das Programm muss heruntergeladen und abgelegt werden.

Anschließend muss in der Konsole in das Verzeichnis `reiseroute` navigiert und folgender Befehl ausgeführt werden:

`./mvnw clean install -DskipTests spring-boot:repackage`

Nachdem der Befehl ausgeführt wurde, befindet sich unter dem Verzeichnis `reseroute/target` die Datei
`reiseroute-1.0.0-spring-boot.jar`.

Durch Ausführen des folgenden Befehls wird die Anwendung gestartet:
`java -jar reiseroute-1.0.0-spring-boot.jar`