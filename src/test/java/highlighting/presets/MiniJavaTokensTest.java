package highlighting.presets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.util.List;
import org.junit.jupiter.api.Test;

class MiniJavaTokensTest {

  @Test
  void stringLiteralsMatchAtStartMiddleAndEnd() {
    String text = "\"start\" x \"middle // still string\" y \"end /* still string */\"";

    assertEquals(
        List.of("\"start\"", "\"middle // still string\"", "\"end /* still string */\""),
        matchedTexts(MiniJavaColours.STRING_LITERAL_COLOUR, text));
  }

  @Test
  void characterLiteralsMatchSingleCharactersOnly() {
    String text = "'a' '\\n' 'ab' ''";

    assertEquals(List.of("'a'", "'\\n'"), matchedTexts(MiniJavaColours.CHAR_LITERAL_COLOUR, text));
  }

  @Test
  void keywordsMatchWholeWordsOnly() {
    String text =
        "package mynewThing; import x; class Sample { public private final return null new"
            + " protected extends void char this className }";

    assertEquals(
        List.of(
            "package",
            "import",
            "class",
            "public",
            "private",
            "final",
            "return",
            "null",
            "new",
            "protected",
            "extends",
            "void",
            "char",
            "this"),
        matchedTexts(MiniJavaColours.KEYWORD_COLOUR, text));
  }

  @Test
  void annotationsMatchAtLineStartAndAfterWhitespace() {
    String text = "@Override\n  @Over-ride\nplain";

    assertEquals(
        List.of("@Override", "@Over-ride"), matchedTexts(MiniJavaColours.ANNOTATION_COLOUR, text));
  }

  @Test
  void commentsMatchByCommentKind() {
    String text = "int x; // public class\n/* block */ x /** docs */";

    assertEquals(
        List.of("// public class"), matchedTexts(MiniJavaColours.LINE_COMMENT_COLOUR, text));
    assertEquals(List.of("/* block */"), matchedTexts(MiniJavaColours.BLOCK_COMMENT_COLOUR, text));
    assertEquals(
        List.of("/** docs */"), matchedTexts(MiniJavaColours.JAVADOC_COMMENT_COLOUR, text));
  }

  @Test
  void tokensReturnNoMatchesForPlainText() {
    String text = "plain identifier text without configured tokens";

    assertTrue(matchedTexts(MiniJavaColours.STRING_LITERAL_COLOUR, text).isEmpty());
    assertTrue(matchedTexts(MiniJavaColours.CHAR_LITERAL_COLOUR, text).isEmpty());
    assertTrue(matchedTexts(MiniJavaColours.ANNOTATION_COLOUR, text).isEmpty());
  }

  private static List<String> matchedTexts(Color colour, String text) {
    return MiniJavaTokens.defaultTokens().stream()
        .filter(token -> token.colour().equals(colour))
        .map(token -> token.test(text))
        .flatMap(List::stream)
        .map(region -> text.substring(region.start(), region.end()))
        .toList();
  }
}
