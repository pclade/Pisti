# Gemeinsamer Kartenkern

Das Gradle-Modul `:cards-core` ist reines Java und hat keine Android-Abhängigkeit.
Die Pisti-App `:app` verwendet dessen `Card`, `CardSet` und `Deck`. Damit lassen
sich Kartensätze und Mischfolgen auch in einem JVM-Simulator verwenden.

`CardSet.french52()` bildet den bisherigen Pisti-Kartensatz ab. Für die
24-Karten-Variante von Altmışaltı kann ein zweites Spiel
`CardSet.french52().onlyRanks("9", "J", "Q", "K", "T", "A")` verwenden. Die
Kartennummern bleiben erhalten: etwa `0` für Pik-Ass, `8` für Pik-Neun und
`51` für Kreuz-König. Dadurch passen die vorhandenen Bilddateien `c00` bis
`c51` weiterhin zu denselben Karten.

`Deck.shuffleDeck(Random)` erlaubt eine reproduzierbare Mischfolge für Tests
und spätere Simulationen. Die Regeln, Kartenwerte und Punkte werden weiterhin
von Pisti festgelegt. Ein 66-Regelwerk kommt separat hinzu; Kartenkern und
Kartengrafiken können beide Spiele nutzen.

| Qualitätsziel | Im ersten Schritt überprüfbar durch |
| --- | --- |
| Modularität | `:cards-core` hat keine Android-Abhängigkeit; `:app` bindet es als Gradle-Modul ein. |
| Wiederverwendbarkeit | Ein zweiter Kartensatz wählt 24 Karten aus denselben Definitionen und behält die Bildkennungen. |
| Portabilität | Kartenkern und Tests laufen auf der JVM ohne Android. |
| Testbarkeit | Feste Misch-Seeds und automatisierte Tests prüfen beide Kartensätze sowie die bestehenden Pisti-Regeln. |

Die Pisti-Spielsteuerung und Wertung befinden sich derzeit noch in `:app`.
Vor dem 66-Spiel werden diese Regeln in einem eigenen Schritt von der
Android-Oberfläche getrennt. Das Kartenmodell besitzt zurzeit veränderbare
Punkt- und Gewichtsfelder für die Kompatibilität mit der bestehenden App;
ihre Verantwortlichkeit soll beim Extrahieren der Regeln überprüft werden.
