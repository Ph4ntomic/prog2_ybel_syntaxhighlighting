# Aufgabe 05: ANTLR und Pretty Printing

## ANTLR-TokenCollector

`AntlrTokenCollector` nutzt den generierten `MiniJavaLexer`. Der Eingabetext
wird ueber `CharStreams.fromString(...)` eingelesen und danach in einen
`CommonTokenStream` umgewandelt. Fuer die farbigen Bereiche werden nur Tokens
uebernommen, die im Projekt auch eine definierte Farbe besitzen:

- Keywords aus der MiniJava-Grammatik,
- String- und Character-Literale,
- Line-, Block- und Javadoc-Kommentare,
- Annotationen ueber `@` und den direkt folgenden Identifier.

Die Start- und Endpositionen stammen direkt aus den ANTLR-Tokens. Da der Lexer
immer eine geordnete Tokenfolge ohne ueberlappende Tokens liefert, muessen die
Hook-Methoden `normalize` und `resolveConflicts` hier nichts mehr veraendern.

## Vergleich zu Blatt 04

Beim Regex-Highlighter werden alle Muster unabhaengig auf den Text angewandt.
Dadurch koennen Konflikte entstehen, etwa wenn ein Keyword innerhalb eines
Kommentars gefunden wird. Der Scanner vermeidet solche Ueberlappungen durch das
Links-nach-rechts-Verfahren mit "laengstes Match gewinnt".

Die ANTLR-Loesung ist bei der eigentlichen Implementierung kuerzer, weil der
Lexer die lexikalische Analyse uebernimmt. Sie ist aber staerker an die Grammatik
gebunden. Ein Unterschied ist zum Beispiel, dass nur Keywords hervorgehoben
werden, die in der MiniJava-Grammatik als eigene Tokens vorkommen. Wo die
Grammatik ein Wort als `IDENTIFIER` behandelt, wird es auch nicht als Keyword
eingefaerbt.

## Pretty Printer

Der Pretty Printer besteht aus `PrettyPrinter` und `PrettyPrinterVisitor`.
`PrettyPrinter` baut Lexer, Parser und Parse-Tree auf. Der Visitor formatiert
anschliessend vor allem die Struktur:

- Klassendeckel und Klassenkoerper,
- Methoden- und Statement-Bloecke,
- Statements mit Semikolon,
- `if`/`else` und `while`.

Beispiel:

```java
class Demo{private String name;public void run(){String x="a";return null;}}
```

wird mit Einrueckung `2` zu:

```java
class Demo {
  private String name;
  public void run() {
    String x = "a";
    return null;
  }
}
```

Ein verschachteltes Beispiel:

```java
class Demo{public void run(){while(active){if(done){return null;}else{{return "open";}}}}}
```

wird zu:

```java
class Demo {
  public void run() {
    while (active) {
      if (done) {
        return null;
      } else {
        {
          return "open";
        }
      }
    }
  }
}
```

Kommentare und urspruengliche Leerzeichen erscheinen in der Pretty-Printer-
Ausgabe nicht wieder. Der Grund ist, dass Whitespace im Lexer uebersprungen wird
und Kommentare auf dem Hidden Channel liegen. Der Visitor arbeitet auf dem
Parse-Tree und sieht dadurch nur die syntaktisch relevanten Tokens.

## Ausfuehrung und Tests

`Main` startet eine kleine Pretty-Printer-Demo und fragt ueber `System.in` nach
der Einrueckungsbreite. Wenn keine Eingabe gelesen werden kann oder die Eingabe
ungueltig ist, wird automatisch `4` verwendet.

Geprueft wird die Loesung durch:

```powershell
.\gradlew.bat test
```

Neue Testklassen:

- `AntlrTokenCollectorTest`
- `PrettyPrinterTest`
- erweiterte `SyntaxHighlightingIntegrationTest`
