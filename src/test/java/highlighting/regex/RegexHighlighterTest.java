package highlighting.regex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import highlighting.core.HighlightRegion;
import highlighting.presets.MiniJavaColours;
import java.awt.Color;
import java.util.List;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class RegexHighlighterTest {

  @Test
  void collectMatchesAppliesAllTokensIndependently() {
    var highlighter =
        new RegexHighlighter(
            List.of(
                Token.of(Pattern.compile("class"), Color.RED),
                Token.of(Pattern.compile("ass"), Color.BLUE)));

    assertEquals(
        List.of(region(0, 5, Color.RED), region(2, 5, Color.BLUE)),
        highlighter.collectMatches("class"));
  }

  @Test
  void resolveConflictsKeepsFirstNonOverlappingRegions() {
    var highlighter = new RegexHighlighter(List.of());

    assertEquals(
        List.of(region(0, 5, Color.RED), region(5, 10, Color.GREEN)),
        highlighter.resolveConflicts(
            List.of(
                region(0, 5, Color.RED),
                region(2, 4, Color.BLUE),
                region(5, 10, Color.GREEN),
                region(8, 12, Color.YELLOW))));
  }

  @Test
  void computeRegionsKeepsCommentInsteadOfKeywordsInsideComment() {
    String text = "// public class";

    assertEquals(
        List.of(region(0, text.length(), MiniJavaColours.LINE_COMMENT_COLOUR)),
        new RegexHighlighter().computeRegions(text));
  }

  @Test
  void computeRegionsKeepsJavadocAsSingleRegion() {
    String text = "/** public class */";

    assertEquals(
        List.of(region(0, text.length(), MiniJavaColours.JAVADOC_COMMENT_COLOUR)),
        new RegexHighlighter().computeRegions(text));
  }

  @Test
  void computeRegionsKeepsAdjacentRegions() {
    String text = "class\"abc\"";

    assertEquals(
        List.of(
            region(0, 5, MiniJavaColours.KEYWORD_COLOUR),
            region(5, 10, MiniJavaColours.STRING_LITERAL_COLOUR)),
        new RegexHighlighter().computeRegions(text));
  }

  @Test
  void computeRegionsReturnsEmptyListForEmptyTextAndTextsWithoutMatches() {
    var highlighter = new RegexHighlighter();

    assertTrue(highlighter.computeRegions("").isEmpty());
    assertTrue(highlighter.computeRegions("identifierOnly").isEmpty());
  }

  private static HighlightRegion region(int start, int end, Color colour) {
    return new HighlightRegion(start, end, colour);
  }
}
