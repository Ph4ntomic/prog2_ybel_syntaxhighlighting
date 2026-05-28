# Dokumentation zu Blatt 04

Diese Dokumentation ist nach Aufgaben getrennt aufgebaut.

## Dateien

- `AUFGABE_01_MINIJAVA_TOKENS.md`
- `AUFGABE_02_REGEX_HIGHLIGHTER.md`
- `AUFGABE_03_SCANNING_HIGHLIGHTER.md`
- `AUFGABE_04_GIT_CI.md`
- `POSTMORTEM_B04.md`

## Kurzstand

Technisch erledigt:

- Aufgabe 1: MiniJava-Tokens und Token-Tests
- Aufgabe 2: RegexHighlighter und Tests
- Aufgabe 3: ScanningHighlighter und Tests
- Aufgabe 4: GitHub Actions CI, getrennte Feature-Branches und PR-Notizen

Lokal geprüft:

```powershell
.\gradlew.bat clean classes test spotlessCheck
.\gradlew.bat test
.\gradlew.bat spotlessCheck
```

Ergebnis:

```text
23 Tests
0 Failures
0 Skipped
BUILD SUCCESSFUL
```

GitHub-Stand:

- `feature/minijava-tokens` wurde über PR #2 gemergt.
- `feature/regex-highlighter` wurde über PR #3 gemergt.
- `feature/scanning-highlighter` wurde über PR #4 gemergt.
- Für die Feature-PRs wurde `CyZeTLC` als Reviewer angefragt.
