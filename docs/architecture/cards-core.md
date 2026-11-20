# Gemeinsamer Kartenkern

Die Java-Bibliothek liegt im eigenständigen Repository
[`pclade/cards-core`](https://github.com/pclade/cards-core), ohne Android-Abhängigkeit.
Die Pisti-App `:app` verwendet dessen `Card`, `CardSet` und `Deck`.
Pisti hält den Bibliotheksstand als Git-Submodul unter `vendor/cards-core` fest.
Gradle bindet ihn mit `includeBuild` als Composite Build ein und ersetzt damit
die deklarierte Koordinate `ch.sakru:cards-core:0.1.0` durch den lokalen Quellcode.
Eine Veröffentlichung in einem Maven-Repository ist noch nicht erfolgt.

`CardSet.french52()` bildet den bisherigen Pisti-Kartensatz ab. Für die
24-Karten-Variante von Altmışaltı kann ein zweites Spiel
`CardSet.french52().onlyRanks("9", "J", "Q", "K", "T", "A")` verwenden. Die
Kartennummern bleiben für Pisti erhalten: etwa `0` für Pik-Ass, `8` für
Pik-Neun und `51` für Kreuz-König. Die Bilddateien `c00` bis `c51` ordnet
Pisti selbst über `PistiArtwork` nach Farbe und Rang zu. Eine Kartenkennung
ist damit keine Bildadresse. Andere Spiele können eigene Motive verwenden.

`CardSet.of(List<Card>)` erlaubt weitere Kartensätze mit eigenen Farben und
Rängen, etwa italienische 40 oder Schweizer 36 Karten. `Deck.shuffleDeck(Random)`
erlaubt reproduzierbare Mischfolgen. `Card` ist unveränderlich; Pisti berechnet
die Kartenpunkte in `PistiScoring`. Ein 66-Regelwerk kommt separat hinzu.

Ein bestehendes Pisti-Checkout wird unter Windows so aktualisiert:

```powershell
git switch master
git pull --ff-only
git submodule update --init --recursive
.\gradlew.bat :cards-core:test testDebugUnitTest assembleDebug
```

Bei einem neuen Checkout: `git clone --recurse-submodules
https://github.com/pclade/Pisti.git`. Änderungen am Kartenkern werden in
seinem eigenen Repository committed; Pisti übernimmt eine neue Version erst,
wenn der Submodul-Commit ausdrücklich aktualisiert wird.

| Qualitätsziel | Im ersten Schritt überprüfbar durch |
| --- | --- |
| Modularität | `cards-core` wird unabhängig von Pisti gebaut; Android und Punkte bleiben ausserhalb. |
| Wiederverwendbarkeit | Ein zweiter Kartensatz wählt 24 Karten aus denselben Definitionen; auch 40 italienische Karten sind definierbar. |
| Portabilität | Kartenkern und Tests laufen auf der JVM ohne Android. |
| Testbarkeit | Feste Misch-Seeds und automatisierte Tests prüfen beide Kartensätze sowie die bestehenden Pisti-Regeln. |

Die Pisti-Spielsteuerung und Wertung befinden sich derzeit noch in `:app`.
Im nächsten Schritt werden sie in `:pisti-game` von der Android-Oberfläche
getrennt. Die Kompatibilität der Oberflächenanimationen muss auf einem
Android-Gerät überprüft werden.
