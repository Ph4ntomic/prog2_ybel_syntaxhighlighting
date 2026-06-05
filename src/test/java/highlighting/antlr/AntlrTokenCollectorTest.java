package highlighting.antlr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import highlighting.core.HighlightRegion;
import highlighting.presets.MiniJavaColours;
import java.awt.Color;
import java.util.List;
import org.junit.jupiter.api.Test;

class AntlrTokenCollectorTest {

  @Test
  void collectMatchesUsesMiniJavaLexerTokens() {
    String text =
        """
        @Demo class Example {
          String s = "x";
          char c = 'y';
          // line
          /** doc */
          /* block */
          return null;
        }
        """;

    var regions = new AntlrTokenCollector().computeRegions(text);

    assertTrue(regions.contains(regionFor(text, "@", MiniJavaColours.ANNOTATION_COLOUR)));
    assertTrue(regions.contains(regionFor(text, "Demo", MiniJavaColours.ANNOTATION_COLOUR)));
    assertTrue(regions.contains(regionFor(text, "class", MiniJavaColours.KEYWORD_COLOUR)));
    assertTrue(regions.contains(regionFor(text, "\"x\"", MiniJavaColours.STRING_LITERAL_COLOUR)));
    assertTrue(regions.contains(regionFor(text, "'y'", MiniJavaColours.CHAR_LITERAL_COLOUR)));
    assertTrue(regions.contains(regionFor(text, "// line", MiniJavaColours.LINE_COMMENT_COLOUR)));
    assertTrue(
        regions.contains(regionFor(text, "/** doc */", MiniJavaColours.JAVADOC_COMMENT_COLOUR)));
    assertTrue(
        regions.contains(regionFor(text, "/* block */", MiniJavaColours.BLOCK_COMMENT_COLOUR)));
    assertTrue(regions.contains(regionFor(text, "return", MiniJavaColours.KEYWORD_COLOUR)));
    assertTrue(regions.contains(regionFor(text, "null", MiniJavaColours.KEYWORD_COLOUR)));
  }

  @Test
  void computeRegionsKeepsLineCommentAsSingleHiddenToken() {
    String text = "// class \"not a string\"";

    assertEquals(
        List.of(region(0, text.length(), MiniJavaColours.LINE_COMMENT_COLOUR)),
        new AntlrTokenCollector().computeRegions(text));
  }

  @Test
  void computeRegionsDoesNotHighlightKeywordTextInsideIdentifier() {
    assertTrue(
        new AntlrTokenCollector().computeRegions("className newValue returnValue").isEmpty());
  }

  @Test
  void tokenStreamRegionsDoNotNeedNormalizationOrConflictResolution() {
    var highlighter = new AntlrTokenCollector();
    var candidates = List.of(region(1, 3, MiniJavaColours.KEYWORD_COLOUR));

    assertSame(candidates, highlighter.normalize(candidates));
    assertSame(candidates, highlighter.resolveConflicts(candidates));
  }

  private static HighlightRegion regionFor(String text, String fragment, Color colour) {
    int start = text.indexOf(fragment);
    assertTrue(start >= 0, "fragment not found: " + fragment);
    return region(start, start + fragment.length(), colour);
  }

  private static HighlightRegion region(int start, int end, Color colour) {
    return new HighlightRegion(start, end, colour);
  }
}
