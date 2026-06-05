package highlighting.antlr;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

public class PrettyPrinterVisitor extends MiniJavaBaseVisitor<Void> {

  private static final String NL = System.lineSeparator();

  private final StringBuilder output = new StringBuilder();
  private final int indentWidth;
  private int indentLevel;
  private boolean atLineStart = true;
  private int previousTokenType = Token.INVALID_TYPE;

  public PrettyPrinterVisitor(int indentWidth) {
    if (indentWidth < 0) {
      throw new IllegalArgumentException("indentWidth must not be negative");
    }
    this.indentWidth = indentWidth;
  }

  public String result() {
    return output.toString().stripTrailing();
  }

  @Override
  public Void visitCompilationUnit(MiniJavaParser.CompilationUnitContext ctx) {
    for (int i = 0; i < ctx.getChildCount(); i++) {
      var child = ctx.getChild(i);
      if (child instanceof TerminalNode terminal && terminal.getSymbol().getType() == Token.EOF) {
        continue;
      }

      visit(child);
      if (child instanceof MiniJavaParser.TypeDeclContext && !atLineStart) {
        nl();
      }
    }
    return null;
  }

  @Override
  public Void visitClassBody(MiniJavaParser.ClassBodyContext ctx) {
    openBlock();

    for (var declaration : ctx.classBodyDeclaration()) {
      visit(declaration);
      if (!atLineStart) {
        nl();
      }
    }

    closeBlock();
    return null;
  }

  @Override
  public Void visitBlock(MiniJavaParser.BlockContext ctx) {
    openBlock();

    for (var statement : ctx.blockStatement()) {
      visit(statement);
      if (!atLineStart) {
        nl();
      }
    }

    closeBlock();
    return null;
  }

  @Override
  public Void visitStatement(MiniJavaParser.StatementContext ctx) {
    if (ctx.block() != null) {
      visit(ctx.block());
      nl();
      return null;
    }

    if (ctx.IF() != null) {
      visitIfStatement(ctx);
      return null;
    }

    if (ctx.WHILE() != null) {
      visitWhileStatement(ctx);
      return null;
    }

    return visitChildren(ctx);
  }

  @Override
  public Void visitTerminal(TerminalNode node) {
    var token = node.getSymbol();
    if (token.getType() == Token.EOF) {
      return null;
    }

    if (token.getType() == MiniJavaParser.SEMI) {
      writeFixed(";", MiniJavaParser.SEMI);
      nl();
      return null;
    }

    writeToken(token.getText(), token.getType());
    return null;
  }

  private void visitIfStatement(MiniJavaParser.StatementContext ctx) {
    writeToken("if", MiniJavaParser.IF);
    space();
    writeFixed("(", MiniJavaParser.LPAREN);
    visit(ctx.expression());
    writeFixed(")", MiniJavaParser.RPAREN);

    var thenStatement = ctx.statement(0);
    visitControlledStatement(thenStatement);

    if (ctx.ELSE() != null) {
      if (!atLineStart) {
        space();
      }
      writeToken("else", MiniJavaParser.ELSE);
      visitControlledStatement(ctx.statement(1));
    }

    if (!atLineStart) {
      nl();
    }
  }

  private void visitWhileStatement(MiniJavaParser.StatementContext ctx) {
    writeToken("while", MiniJavaParser.WHILE);
    space();
    writeFixed("(", MiniJavaParser.LPAREN);
    visit(ctx.expression());
    writeFixed(")", MiniJavaParser.RPAREN);
    visitControlledStatement(ctx.statement(0));

    if (!atLineStart) {
      nl();
    }
  }

  private void visitControlledStatement(MiniJavaParser.StatementContext statement) {
    if (statement.block() != null) {
      space();
      visit(statement.block());
      return;
    }

    nl();
    indentLevel++;
    visit(statement);
    indentLevel--;
  }

  private void openBlock() {
    space();
    writeFixed("{", MiniJavaParser.LBRACE);
    nl();
    indentLevel++;
  }

  private void closeBlock() {
    indentLevel--;
    writeFixed("}", MiniJavaParser.RBRACE);
  }

  private void writeToken(String text, int tokenType) {
    if (needsSpaceBefore(tokenType)) {
      space();
    }
    writeFixed(text, tokenType);
  }

  private void writeFixed(String text, int tokenType) {
    write(text);
    previousTokenType = tokenType;
  }

  private void write(String text) {
    if (text.isEmpty()) {
      return;
    }

    if (atLineStart) {
      output.append(" ".repeat(indentLevel * indentWidth));
      atLineStart = false;
    }
    output.append(text);
  }

  private void nl() {
    removeTrailingSpaces();
    if (output.length() == 0 || output.charAt(output.length() - 1) == '\n') {
      atLineStart = true;
      previousTokenType = Token.INVALID_TYPE;
      return;
    }

    output.append(NL);
    atLineStart = true;
    previousTokenType = Token.INVALID_TYPE;
  }

  private void space() {
    if (atLineStart || output.length() == 0) {
      return;
    }

    var last = output.charAt(output.length() - 1);
    if (!Character.isWhitespace(last)) {
      output.append(' ');
    }
  }

  private void removeTrailingSpaces() {
    while (output.length() > 0 && output.charAt(output.length() - 1) == ' ') {
      output.deleteCharAt(output.length() - 1);
    }
  }

  private boolean needsSpaceBefore(int tokenType) {
    if (atLineStart || previousTokenType == Token.INVALID_TYPE) {
      return false;
    }

    if (tokenType == MiniJavaParser.LPAREN) {
      return previousTokenType == MiniJavaParser.IF || previousTokenType == MiniJavaParser.WHILE;
    }

    if (hasNoSpaceBefore(tokenType) || hasNoSpaceAfter(previousTokenType)) {
      return false;
    }

    return isOperator(tokenType)
        || isOperator(previousTokenType)
        || previousTokenType == MiniJavaParser.COMMA
        || previousTokenType == MiniJavaParser.RBRACK
        || (isWordLike(previousTokenType) && isWordLike(tokenType));
  }

  private static boolean hasNoSpaceBefore(int tokenType) {
    return switch (tokenType) {
      case MiniJavaParser.RPAREN,
          MiniJavaParser.RBRACK,
          MiniJavaParser.COMMA,
          MiniJavaParser.SEMI,
          MiniJavaParser.DOT ->
          true;
      default -> false;
    };
  }

  private static boolean hasNoSpaceAfter(int tokenType) {
    return switch (tokenType) {
      case MiniJavaParser.LPAREN, MiniJavaParser.LBRACK, MiniJavaParser.DOT, MiniJavaParser.AT ->
          true;
      default -> false;
    };
  }

  private static boolean isWordLike(int tokenType) {
    return switch (tokenType) {
      case MiniJavaParser.PACKAGE,
          MiniJavaParser.IMPORT,
          MiniJavaParser.CLASS,
          MiniJavaParser.PUBLIC,
          MiniJavaParser.PRIVATE,
          MiniJavaParser.FINAL,
          MiniJavaParser.RETURN,
          MiniJavaParser.NULL,
          MiniJavaParser.NEW,
          MiniJavaParser.IF,
          MiniJavaParser.ELSE,
          MiniJavaParser.WHILE,
          MiniJavaParser.EXTENDS,
          MiniJavaParser.IMPLEMENTS,
          MiniJavaParser.IDENTIFIER,
          MiniJavaParser.STRING_LITERAL,
          MiniJavaParser.CHAR_LITERAL ->
          true;
      default -> false;
    };
  }

  private static boolean isOperator(int tokenType) {
    return switch (tokenType) {
      case MiniJavaParser.T__0,
          MiniJavaParser.PLUS,
          MiniJavaParser.MINUS,
          MiniJavaParser.STAR,
          MiniJavaParser.SLASH,
          MiniJavaParser.PERCENT,
          MiniJavaParser.ASSIGN,
          MiniJavaParser.LT,
          MiniJavaParser.GT,
          MiniJavaParser.LE,
          MiniJavaParser.GE,
          MiniJavaParser.EQUAL,
          MiniJavaParser.NOTEQUAL,
          MiniJavaParser.AND,
          MiniJavaParser.OR ->
          true;
      default -> false;
    };
  }
}
