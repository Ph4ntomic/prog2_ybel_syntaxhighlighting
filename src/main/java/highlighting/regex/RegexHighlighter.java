package highlighting.regex;

import highlighting.core.HighlightRegion;
import highlighting.core.SyntaxHighlighter;
import highlighting.presets.MiniJavaTokens;
import java.util.ArrayList;
import java.util.List;

public class RegexHighlighter extends SyntaxHighlighter {
  private final List<Token> tokens;

  public RegexHighlighter() {
    this(MiniJavaTokens.defaultTokens());
  }

  public RegexHighlighter(List<Token> tokens) {
    this.tokens = List.copyOf(tokens);
  }

  @Override
  public List<HighlightRegion> collectMatches(String text) {
    var regions = new ArrayList<HighlightRegion>();

    for (Token token : tokens) {
      regions.addAll(token.test(text));
    }

    return regions;
  }

  @Override
  public List<HighlightRegion> resolveConflicts(List<HighlightRegion> regions) {
    var selected = new ArrayList<HighlightRegion>();
    int coveredUntil = 0;

    for (HighlightRegion region : regions) {
      if (region.start() >= coveredUntil) {
        selected.add(region);
        coveredUntil = region.end();
      }
    }

    return selected;
  }
}
