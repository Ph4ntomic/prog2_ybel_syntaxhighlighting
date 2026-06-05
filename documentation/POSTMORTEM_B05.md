Post Mortem: Blatt 05

Zusammenfassung:

Ich habe in Blatt 05 an zwei Projekten gearbeitet. Im Syntaxhighlighting-Projekt habe ich den AntlrTokenCollector umgesetzt und dafür den von ANTLR generierten MiniJavaLexer verwendet. Dadurch werden Keywords, Literale, Kommentare und Annotationen nicht mehr über eigene Regex-Patterns gesucht, sondern direkt aus dem Tokenstream abgeleitet. Außerdem habe ich einen einfachen Pretty-Printer mit ANTLR-Parser und Visitor gebaut. Dabei erzeugt der Parser zuerst einen Parse-Tree, der anschließend vom Visitor durchlaufen wird. Das Ergebnis ist MiniJava-Code mit konsistenten Blöcken, Zeilenumbrüchen und Einrückungen. Zusätzlich habe ich die Konsolen-Demo so angepasst, dass die Einrückungsbreite beim Ausführen übergeben werden kann.

Im Cycle-Chronicles-Projekt habe ich Äquivalenzklassen und Grenzwerte für Shop#accept abgeleitet und daraus JUnit-Tests mit Mockito erstellt. Als Bonus habe ich repair und deliver implementiert und ebenfalls getestet.

Details:

Besonders interessant fand ich den Unterschied zwischen Regex, Scanner und ANTLR. Bei der ANTLR-Variante entstehen praktisch keine überlappenden Highlight-Regionen mehr, weil der Lexer bereits eine eindeutige Tokenfolge liefert. Dadurch ist die Lösung robuster als einzelne Regex-Treffer, die sich gegenseitig überschneiden können. Beim Pretty-Printer ist mir aufgefallen, dass der Parse-Tree nicht mehr den kompletten Originaltext enthält. Kommentare und ursprüngliche Leerzeichen fehlen, weil sie übersprungen oder auf den Hidden Channel gelegt werden. Deshalb kann der Pretty-Printer den Code nicht einfach originalgetreu zurückgeben, sondern muss eine neue, einheitliche Formatierung erzeugen.

Im Cycle-Chronicles-Projekt war wichtig, die Testfälle nicht nur aus dem Bauch heraus zu schreiben, sondern systematisch aus gültigen, ungültigen und grenznahen Eingaben abzuleiten. Dadurch wurden die Tests nachvollziehbarer und decken typische Fehlerstellen besser ab.

Reflexion: schwierigster Teil

Am schwierigsten war für mich die Abgrenzung beim Pretty Printing. Ich musste entscheiden, welche Regeln ich wirklich selbst formatiere und wo der normale Besuch der Kindknoten reicht. Gelöst habe ich das, indem ich mich auf Klassenkörper, Blöcke, if/else, while und Semikolon-Statements konzentriert habe. Dadurch blieb die Lösung überschaubar und trotzdem für die Aufgabe sinnvoll.

Reflexion: gelernt

Ich habe besser verstanden, warum ein Lexer beim Highlighting robuster ist als einzelne Regex-Treffer. Beim Mocking habe ich gelernt, dass ich nicht die zu testende Klasse Shop mocken darf, sondern nur die unvollständige Abhängigkeit Order. Dadurch testen die Unit-Tests weiterhin die echte Shop-Logik, während Order kontrolliert ersetzt wird.

Links:

Portfolio B05:
https://github.com/Ph4ntomic/prog2-portfolio-sose2026-leonardo-parrino/tree/main/B05

Syntaxhighlighting-Projekt:
https://github.com/Ph4ntomic/prog2_ybel_syntaxhighlighting/tree/master

Syntaxhighlighting B05-Dokumentation:
https://github.com/Ph4ntomic/prog2_ybel_syntaxhighlighting/blob/master/documentation/AUFGABE_05_ANTLR_PRETTY_PRINTING.md

Cycle-Chronicles-Projekt:
https://github.com/Ph4ntomic/prog2_ybel_cyclechronicles/tree/master

Cycle-Chronicles Analyse:
https://github.com/Ph4ntomic/prog2_ybel_cyclechronicles/blob/master/documentation/AUFGABE_05_ACCEPT_ANALYSIS.md

Review-Nachweise:
https://github.com/Ph4ntomic/prog2_ybel_syntaxhighlighting/tree/master/documentation/nachweisreviews
