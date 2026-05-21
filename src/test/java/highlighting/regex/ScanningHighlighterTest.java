package highlighting.regex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import highlighting.core.HighlightRegion;
import highlighting.presets.MiniJavaColours;
import java.awt.Color;
import java.util.List;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class ScanningHighlighterTest {

  @Test
  void collectMatchesChoosesLongestMatchAtCurrentPosition() {
    var highlighter =
        new ScanningHighlighter(
            List.of(
                Token.of(Pattern.compile("ab"), Color.RED),
                Token.of(Pattern.compile("abc"), Color.BLUE)));

    assertEquals(List.of(region(1, 4, Color.BLUE)), highlighter.collectMatches("xabc"));
  }

  @Test
  void collectMatchesKeepsEarlierTokenWhenMatchesHaveSameLength() {
    var highlighter =
        new ScanningHighlighter(
            List.of(
                Token.of(Pattern.compile("abc"), Color.RED),
                Token.of(Pattern.compile("abc"), Color.BLUE)));

    assertEquals(List.of(region(0, 3, Color.RED)), highlighter.collectMatches("abc"));
  }

  @Test
  void collectMatchesSkipsUnmatchedTextAndContinuesScanning() {
    var highlighter =
        new ScanningHighlighter(
            List.of(
                Token.of(Pattern.compile("ab"), Color.RED),
                Token.of(Pattern.compile("abc"), Color.BLUE)));

    assertEquals(
        List.of(region(1, 3, Color.RED), region(4, 7, Color.BLUE)),
        highlighter.collectMatches("zabzabc"));
  }

  @Test
  void computeRegionsKeepsStringsAndCommentsAsSeparateScannerTokens() {
    String text = "\"// not comment\" // public";
    int commentStart = text.indexOf("// public");

    assertEquals(
        List.of(
            region(0, "\"// not comment\"".length(), MiniJavaColours.STRING_LITERAL_COLOUR),
            region(commentStart, text.length(), MiniJavaColours.LINE_COMMENT_COLOUR)),
        new ScanningHighlighter().computeRegions(text));
  }

  @Test
  void computeRegionsDoesNotMatchKeywordInsideIdentifier() {
    assertTrue(new ScanningHighlighter().computeRegions("mynew identifier").isEmpty());
  }

  @Test
  void normalizeReturnsCandidatesUnchanged() {
    var highlighter = new ScanningHighlighter(List.of());
    var candidates = List.of(region(3, 6, Color.RED));

    assertSame(candidates, highlighter.normalize(candidates));
  }

  private static HighlightRegion region(int start, int end, Color colour) {
    return new HighlightRegion(start, end, colour);
  }
}
