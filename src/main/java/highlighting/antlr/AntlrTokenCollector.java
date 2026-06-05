package highlighting.antlr;

import highlighting.core.HighlightRegion;
import highlighting.core.SyntaxHighlighter;
import highlighting.presets.MiniJavaColours;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

public class AntlrTokenCollector extends SyntaxHighlighter {

  @Override
  public List<HighlightRegion> collectMatches(String text) {
    var lexer = new MiniJavaLexer(CharStreams.fromString(text));
    lexer.removeErrorListeners();
    var tokenStream = new CommonTokenStream(lexer);
    tokenStream.fill();

    var regions = new ArrayList<HighlightRegion>();
    boolean nextIdentifierBelongsToAnnotation = false;

    for (Token token : tokenStream.getTokens()) {
      if (token.getType() == Token.EOF) {
        break;
      }

      if (token.getType() == MiniJavaLexer.AT) {
        regions.add(toRegion(token, MiniJavaColours.ANNOTATION_COLOUR));
        nextIdentifierBelongsToAnnotation = true;
        continue;
      }

      if (nextIdentifierBelongsToAnnotation && token.getType() == MiniJavaLexer.IDENTIFIER) {
        regions.add(toRegion(token, MiniJavaColours.ANNOTATION_COLOUR));
        nextIdentifierBelongsToAnnotation = false;
        continue;
      }
      nextIdentifierBelongsToAnnotation = false;

      var colour =
          switch (token.getType()) {
            case MiniJavaLexer.PACKAGE,
                MiniJavaLexer.IMPORT,
                MiniJavaLexer.CLASS,
                MiniJavaLexer.PUBLIC,
                MiniJavaLexer.PRIVATE,
                MiniJavaLexer.FINAL,
                MiniJavaLexer.RETURN,
                MiniJavaLexer.NULL,
                MiniJavaLexer.NEW,
                MiniJavaLexer.IF,
                MiniJavaLexer.ELSE,
                MiniJavaLexer.WHILE,
                MiniJavaLexer.EXTENDS,
                MiniJavaLexer.IMPLEMENTS ->
                MiniJavaColours.KEYWORD_COLOUR;
            case MiniJavaLexer.STRING_LITERAL -> MiniJavaColours.STRING_LITERAL_COLOUR;
            case MiniJavaLexer.CHAR_LITERAL -> MiniJavaColours.CHAR_LITERAL_COLOUR;
            case MiniJavaLexer.LINE_COMMENT -> MiniJavaColours.LINE_COMMENT_COLOUR;
            case MiniJavaLexer.JAVADOC_COMMENT -> MiniJavaColours.JAVADOC_COMMENT_COLOUR;
            case MiniJavaLexer.BLOCK_COMMENT -> MiniJavaColours.BLOCK_COMMENT_COLOUR;
            default -> null;
          };

      if (colour != null) {
        regions.add(toRegion(token, colour));
      }
    }

    return regions;
  }

  @Override
  public List<HighlightRegion> normalize(List<HighlightRegion> candidates) {
    return candidates;
  }

  @Override
  public List<HighlightRegion> resolveConflicts(List<HighlightRegion> normalized) {
    return normalized;
  }

  private static HighlightRegion toRegion(Token token, Color colour) {
    return new HighlightRegion(token.getStartIndex(), token.getStopIndex() + 1, colour);
  }
}
