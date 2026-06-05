package highlighting.antlr;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PrettyPrinterTest {

  private static final String NL = System.lineSeparator();

  @Test
  void formatsClassMembersAndMethodBlocks() {
    String source =
        "class Demo{private String name;public void run(){String x=\"a\";return null;}}";

    assertEquals(
        lines(
            "class Demo {",
            "  private String name;",
            "  public void run() {",
            "    String x = \"a\";",
            "    return null;",
            "  }",
            "}"),
        PrettyPrinter.format(source, 2));
  }

  @Test
  void formatsIfElseWhileAndNestedBlocks() {
    String source =
        "class Demo{public void run(){while(active){if(done){return null;}else{{return"
            + " \"open\";}}}}}";

    assertEquals(
        lines(
            "class Demo {",
            "  public void run() {",
            "    while (active) {",
            "      if (done) {",
            "        return null;",
            "      } else {",
            "        {",
            "          return \"open\";",
            "        }",
            "      }",
            "    }",
            "  }",
            "}"),
        PrettyPrinter.format(source, 2));
  }

  @Test
  void keepsPackageImportsAndTypesOnSeparateLines() {
    String source = "package demo;import foo.Bar;class Demo{}";

    assertEquals(
        lines("package demo;", "import foo.Bar;", "class Demo {", "}"),
        PrettyPrinter.format(source, 2));
  }

  @Test
  void indentsControlledStatementWithoutExplicitBlock() {
    String source = "class Demo{public void run(){if(done)return null;}}";

    assertEquals(
        lines(
            "class Demo {",
            "  public void run() {",
            "    if (done)",
            "      return null;",
            "  }",
            "}"),
        PrettyPrinter.format(source, 2));
  }

  @Test
  void keepsSpaceAfterAnnotationWithArguments() {
    String source = "class Demo{@Anno(value)public String text(){return \"ok\";}}";

    assertEquals(
        lines(
            "class Demo {",
            "  @Anno(value) public String text() {",
            "    return \"ok\";",
            "  }",
            "}"),
        PrettyPrinter.format(source, 2));
  }

  @Test
  void keepsSpaceBeforeAnnotationAfterModifier() {
    String source = "class Demo{public @Anno String text(){return \"ok\";}}";

    assertEquals(
        lines("class Demo {", "  public @Anno String text() {", "    return \"ok\";", "  }", "}"),
        PrettyPrinter.format(source, 2));
  }

  private static String lines(String... lines) {
    return String.join(NL, lines);
  }
}
