package highlighting.antlr;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public final class PrettyPrinter {

  private PrettyPrinter() {}

  public static MiniJavaParser.CompilationUnitContext parse(String source) {
    var lexer = new MiniJavaLexer(CharStreams.fromString(source));
    lexer.removeErrorListeners();
    var tokens = new CommonTokenStream(lexer);
    var parser = new MiniJavaParser(tokens);
    parser.removeErrorListeners();
    return parser.compilationUnit();
  }

  public static String format(String source, int indentWidth) {
    var tree = parse(source);
    var visitor = new PrettyPrinterVisitor(indentWidth);
    visitor.visit(tree);
    return visitor.result();
  }
}
