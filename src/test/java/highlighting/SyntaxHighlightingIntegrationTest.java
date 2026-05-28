package highlighting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import highlighting.core.HighlightRegion;
import highlighting.core.SyntaxHighlighter;
import highlighting.presets.MiniJavaColours;
import highlighting.presets.Texts;
import highlighting.regex.RegexHighlighter;
import highlighting.regex.ScanningHighlighter;
import java.awt.Color;
import java.util.List;
import org.junit.jupiter.api.Test;

class SyntaxHighlightingIntegrationTest {

  @Test
  void regexHighlighterProducesValidRegionsForStartText() {
    assertValidRegions(new RegexHighlighter().computeRegions(Texts.START_TEXT), Texts.START_TEXT);
  }

  @Test
  void scanningHighlighterProducesValidRegionsForStartText() {
    assertValidRegions(
        new ScanningHighlighter().computeRegions(Texts.START_TEXT), Texts.START_TEXT);
  }

  @Test
  void regexHighlighterCoversAllConfiguredTokenKindsInStartText() {
    assertContainsConfiguredTokenKinds(new RegexHighlighter());
  }

  @Test
  void scanningHighlighterCoversAllConfiguredTokenKindsInStartText() {
    assertContainsConfiguredTokenKinds(new ScanningHighlighter());
  }

  @Test
  void bothHighlightersKeepLineCommentBeforeBlockCommentMarkerInsideLineComment() {
    String text = "// line comment with /* block marker */";

    assertEquals(
        List.of(new HighlightRegion(0, text.length(), MiniJavaColours.LINE_COMMENT_COLOUR)),
        new RegexHighlighter().computeRegions(text));
    assertEquals(
        List.of(new HighlightRegion(0, text.length(), MiniJavaColours.LINE_COMMENT_COLOUR)),
        new ScanningHighlighter().computeRegions(text));
  }

  private static void assertValidRegions(List<HighlightRegion> regions, String text) {
    assertFalse(regions.isEmpty());

    int previousEnd = 0;
    for (HighlightRegion region : regions) {
      assertTrue(region.start() >= previousEnd);
      assertTrue(region.start() < region.end());
      assertTrue(region.end() <= text.length());
      previousEnd = region.end();
    }
  }

  private static void assertContainsConfiguredTokenKinds(SyntaxHighlighter highlighter) {
    List<HighlightRegion> regions = highlighter.computeRegions(Texts.START_TEXT);

    assertTrue(containsColour(regions, MiniJavaColours.STRING_LITERAL_COLOUR));
    assertTrue(containsColour(regions, MiniJavaColours.CHAR_LITERAL_COLOUR));
    assertTrue(containsColour(regions, MiniJavaColours.KEYWORD_COLOUR));
    assertTrue(containsColour(regions, MiniJavaColours.ANNOTATION_COLOUR));
    assertTrue(containsColour(regions, MiniJavaColours.LINE_COMMENT_COLOUR));
    assertTrue(containsColour(regions, MiniJavaColours.BLOCK_COMMENT_COLOUR));
    assertTrue(containsColour(regions, MiniJavaColours.JAVADOC_COMMENT_COLOUR));
  }

  private static boolean containsColour(List<HighlightRegion> regions, Color colour) {
    return regions.stream().anyMatch(region -> region.colour().equals(colour));
  }
}
