package highlighting.presets;

import highlighting.regex.Token;
import java.util.List;
import java.util.regex.Pattern;

public final class MiniJavaTokens {

  private static final Pattern JAVADOC_COMMENT = Pattern.compile("/\\*\\*[\\s\\S]*?\\*/");
  private static final Pattern BLOCK_COMMENT = Pattern.compile("/\\*(?!\\*)[\\s\\S]*?\\*/");
  private static final Pattern LINE_COMMENT = Pattern.compile("//.*");
  private static final Pattern STRING_LITERAL = Pattern.compile("\"([^\"\\\\]|\\\\.)*\"");
  private static final Pattern CHAR_LITERAL =
      Pattern.compile("(?<![\\w\\\\'])'(?:[^'\\\\\\r\\n]|\\\\.)'(?![\\w'])");
  private static final Pattern ANNOTATION = Pattern.compile("@[A-Za-z-]+");
  private static final Pattern KEYWORD =
      Pattern.compile(
          "\\b(?:package|import|class|public|private|final|return|null|new|protected|extends|void|char|this)\\b");

  public static List<Token> defaultTokens() {
    return List.of(
        Token.of(JAVADOC_COMMENT, MiniJavaColours.JAVADOC_COMMENT_COLOUR),
        Token.of(BLOCK_COMMENT, MiniJavaColours.BLOCK_COMMENT_COLOUR),
        Token.of(LINE_COMMENT, MiniJavaColours.LINE_COMMENT_COLOUR),
        Token.of(STRING_LITERAL, MiniJavaColours.STRING_LITERAL_COLOUR),
        Token.of(CHAR_LITERAL, MiniJavaColours.CHAR_LITERAL_COLOUR),
        Token.of(ANNOTATION, MiniJavaColours.ANNOTATION_COLOUR),
        Token.of(KEYWORD, MiniJavaColours.KEYWORD_COLOUR));
  }
}
