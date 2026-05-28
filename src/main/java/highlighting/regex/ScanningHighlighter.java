package highlighting.regex;

import highlighting.core.HighlightRegion;
import highlighting.core.SyntaxHighlighter;
import highlighting.presets.MiniJavaTokens;
import java.util.ArrayList;
import java.util.List;

public class ScanningHighlighter extends SyntaxHighlighter {
  private final List<Token> tokens;

  public ScanningHighlighter() {
    this(MiniJavaTokens.defaultTokens());
  }

  public ScanningHighlighter(List<Token> tokens) {
    this.tokens = List.copyOf(tokens);
  }

  @Override
  public List<HighlightRegion> collectMatches(String text) {
    var regions = new ArrayList<HighlightRegion>();
    int index = 0;

    while (index < text.length()) {
      HighlightRegion bestRegion = null;
      int bestEnd = index;

      for (Token token : tokens) {
        var matcher = token.pattern().matcher(text);
        matcher.region(index, text.length());
        matcher.useTransparentBounds(true);

        if (matcher.lookingAt() && matcher.end() > bestEnd) {
          int highlightedStart = matcher.start(token.matchingGroup());
          int highlightedEnd = matcher.end(token.matchingGroup());

          if (highlightedStart >= 0 && highlightedStart < highlightedEnd) {
            bestRegion = new HighlightRegion(highlightedStart, highlightedEnd, token.colour());
            bestEnd = matcher.end();
          }
        }
      }

      if (bestRegion == null) {
        index++;
      } else {
        regions.add(bestRegion);
        index = bestEnd;
      }
    }

    return regions;
  }

  @Override
  public List<HighlightRegion> normalize(List<HighlightRegion> candidates) {
    return candidates;
  }
}
