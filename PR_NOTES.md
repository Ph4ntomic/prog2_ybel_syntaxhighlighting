# PR notes for Blatt 04

Use these notes as short pull request descriptions and review checklists.

## MiniJava tokens

Summary:

- Defines tokens for Javadoc comments, block comments, line comments, string literals, character
  literals, annotations, and MiniJava keywords.
- Keeps keywords word-boundary based so identifiers such as `mynew` and `className` are not
  highlighted as keywords.
- Tightens character literal matching so invalid longer literals do not create accidental partial
  matches.

Review focus:

- Token order: comments before literals, literals before annotations and keywords.
- Escaped string and char literals.
- Whole-word keyword handling.

## Regex highlighter

Summary:

- Applies all tokens independently to the whole input.
- Normalizes candidates via the template method and removes overlapping regions afterwards.
- Keeps adjacent half-open regions such as `[0, 5)` and `[5, 10)`.

Review focus:

- Conflict resolution for keywords inside comments.
- Comment and Javadoc regions remaining single highlight regions.
- Tests for direct `collectMatches` and final `computeRegions` behavior.

## Scanning highlighter

Summary:

- Scans left to right and picks the longest token at the current index.
- Keeps the earlier token when two matches have the same length.
- Uses transparent matcher bounds so word boundaries and lookbehinds work correctly with
  `Matcher.region(...)`.

Review focus:

- Longest-match behavior.
- Tie handling by token order.
- No false keyword matches inside identifiers.

## CI

Summary:

- Adds a GitHub Actions workflow with separate `build`, `test`, and `format` jobs.
- Runs on pull requests and manual dispatch.
- Uses Java 25 and the Gradle wrapper.

Local verification:

```sh
./gradlew classes test spotlessCheck
```
