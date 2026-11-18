# Pisti und Altmışaltı: Zielarchitektur

Status: Architekturvorschlag, 18.11.2026. Altmışaltı folgt der vom Nutzer
gewählten türkischen [24-Karten-Variante](https://tr.wikipedia.org/wiki/Altm%C4%B1%C5%9Falt%C4%B1).

## Projekte und Abhängigkeiten

| Git-Projekt | Module | Aufgabe |
| --- | --- | --- |
| `Pisti` | `:app` (Android), später `:pisti-game` (Java) | Pisti-Oberfläche, eigene Spielregeln, Wertung und Gegnerstrategie |
| `cards-core` | Java-Bibliothek | Kartenidentität, Kartensatz-Definitionen, Mischen und Ziehen; keine Android- oder Spielregeln |
| `altmisalti` | `:app` (Android), `:sixtysix-game` (Java) | Eigenständiges 66-Spiel mit Regeln, Wertung, Gegnerstrategie und Oberfläche |

Weitere Spiele wie Briscola oder Jass erhalten bei Bedarf eigene Spielprojekte.
Sie nutzen dieselbe Kartenbibliothek mit ihrem jeweiligen Kartensatz.

Die beiden Spielprojekte hängen von `cards-core` ab. Es gibt keine Abhängigkeit
zwischen Pisti und Altmışaltı. Der Kartenkern wird versioniert und als
Bibliothek bezogen. Für die gleichzeitige lokale Entwicklung separater
Gradle-Builds kann ein [Composite Build](https://docs.gradle.org/current/userguide/composite_builds.html)
die veröffentlichte Bibliothek durch ihren lokalen Quellcode ersetzen. Ein
Projekt darf dafür kein `project(':cards-core')` eines anderen Builds erwarten.

## Verantwortlichkeiten

- **Kartenkern:** Karte (Farbe, Rang, stabile Kennung), Auswahl des Kartensatzes
  und reproduzierbares Mischen. `french52()` liefert die bisherigen Pisti-Karten;
  `onlyRanks("9", "J", "Q", "K", "T", "A")` liefert die 24 Karten für 66.
  Kartenwerte, Trumpf und Spielpunkte gehören in die jeweiligen Regeln.
- **Pisti-Regeln:** Austeilen, Zugfolge, Stich durch gleichen Rang oder Buben,
  Pisti und Punkte. Eine Android-unabhängige Strategie wählt aus den legalen
  Zügen; später kann eine gelernte Strategie dieselbe Schnittstelle nutzen.
- **66-Regeln:** Sechs Handkarten, Talon und Trumpf, Stiche, Farb- und Trumpfpflicht
  nach Aufbrauchen/Schliessen des Talons, Eşleşme, Trumpf-Neun-Tausch,
  Schliessen, Ansage von 66 und Spielpunkte. Aktionen umfassen mehr als das
  Ausspielen einer Karte.
- **Android-Oberflächen:** zeigen den jeweiligen Zustand und reagieren auf
  Spielereignisse. Pisti zeigt vier Handkarten und einen Tischstapel, 66 sechs
  Handkarten, Stichkarten und einen Trumpf/Talon. Grafiken können wiederverwendet
  werden; die Anordnung und Animationen werden pro Spiel gestaltet.

## Erweiterbare Kartensätze und Motive

`cards-core` wächst durch stabile Begriffe und Schnittstellen: Kartenidentität
aus Farbe und Rang, frei definierbare Kartensätze, Ziehen und Mischen. Ein
Kartensatz ist eine Liste von Karten, keine fest eingebaute 52er-Annahme.
Definitionen für französische 52 und 24 Karten, italienische 40 Karten und
Schweizer Jasskarten mit 36 Karten können als Daten oder kleine Java-Fabriken
ergänzt werden. Spielstärke und Punktwert legt das jeweilige Regelmodul fest;
beispielsweise ist die Drei bei Briscola stark, obwohl sie nur den Rang 3 trägt.

Kartenbilder gehören nicht in die Java-Bibliothek. Ein Android-Bildpaket oder
die jeweilige App ordnet einer logischen Kartenidentität ein Bild zu, etwa
`italian/bastoni/asso` oder `swiss/rosen/under`. Eine kleine Schnittstelle
`CardArtworkProvider` kann die Zuordnung für die Android-Oberflächen kapseln.
Die heutige Pisti-Zuordnung über `c00` bis `c51` bleibt während der Migration
als Adapter erhalten; langfristig ist eine Kartennummer keine Bildadresse.
Ein gemeinsam genutztes Android-Bildpaket lohnt sich, sobald mehrere Apps
tatsächlich dieselben Motive verwenden.

Die drei vom Nutzer gelieferten italienischen Bögen enthalten 18 + 18 + 4
Kartenbilder: je zehn Karten in Bastoni, Spade, Denari und Coppe (Ass, 2–7,
Fante, Cavallo, Re). Die einzelnen Zuschnitte werden als separates Motivpaket
mit eindeutigen Dateinamen abgelegt. Die Originalbögen bleiben erhalten.
Jass mit Deutschschweizer oder französischen Figuren benötigt einen eigenen
36-Karten-Satz und passende Bildzuordnungen; die Jass-Spielarten gehören
in ein eigenes Regelmodul.

Regelreferenzen: [Briscola (Modiano)](https://www.modiano.it/en/game/briscola/)
und [Schweizer Jasskarten (Swisslos)](https://www.swisslos.ch/de/jass/informationen/jass-regeln/jass-grundlagen.html).

Die Regelkerne liefern `legalActions(state)` und `apply(state, action)` sowie
einen für den jeweiligen Spieler sichtbaren Beobachtungszustand. Eine
Strategie-Schnittstelle wählt aus den erlaubten Aktionen. Bei 66 sind auch
Ansagen, Tauschen und Schliessen Aktionen. Das genaue gemeinsame Interface
wird nach der Extraktion der Pisti-Regeln und einem ersten 66-Regeltest
festgelegt; beide Spiele müssen nicht dieselbe Zustandsklasse verwenden.

## Umsetzung in überprüfbaren Schritten

1. Pisti-Verhalten mit Tests absichern und `:cards-core` im Pisti-Projekt
   erproben (Entwurf-PR #4).
2. `cards-core` als eigenständige Java-Bibliothek herauslösen. Pisti bezieht
   eine festgelegte Version. Die heute noch veränderbaren Punkt- und
   Gewichtsfelder der Karte werden dabei in die Spielregeln verlagert. Die
   heute an `c00` bis `c51` gebundene Nummer wird von Bildressourcen entkoppelt.
3. Pisti-Zugfolge und Wertung aus `MainActivity` in `:pisti-game` verschieben;
   bisherige Partien und Animationen müssen gleich funktionieren.
4. Eigenständiges 66-Projekt mit `:sixtysix-game`, Regeltests und Android-App
   erstellen; denselben veröffentlichten Kartenkern nutzen.
5. Erst nach einem Vergleich beider Oberflächen gemeinsam nutzbare
   Android-Kartenbausteine gegebenenfalls in eine separate UI-Bibliothek
   auslagern.
6. Für Briscola den italienischen 40-Karten-Satz und sein Bildpaket ergänzen;
   die Regeln und Oberfläche bleiben in einem eigenen Spielprojekt. Jass kann
   später den 36-Karten-Satz als weiteren Testfall einbringen.

## Qualitätsnachweise für den Unterricht

| Qualitätsaspekt | Beobachtbarer Nachweis |
| --- | --- |
| Modularität | Regeln beider Spiele können ohne Android kompiliert und getestet werden. |
| Wiederverwendbarkeit | Beide Apps verwenden dieselbe versionierte Kartenbibliothek. |
| Änderbarkeit | Eine Änderung der 66-Wertung erfordert keine Änderung an Pisti oder `cards-core`. |
| Portabilität | Die Regelkerne laufen auf Android und in einem JVM-Simulator mit denselben Tests. |
| Testbarkeit | Feste Misch-Seeds und Regeltests reproduzieren vollständige Spielverläufe. |
