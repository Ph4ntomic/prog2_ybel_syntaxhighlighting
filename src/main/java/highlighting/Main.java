package highlighting;

import highlighting.antlr.AntlrTokenCollector;
import highlighting.antlr.PrettyPrinter;
import highlighting.core.SyntaxHighlighter;
import highlighting.presets.Texts;
import highlighting.ui.EditorUI;
import java.util.Scanner;

public class Main {

  private static final String PRETTY_PRINTER_SAMPLE =
      """
      class Demo{private String name;public void run(){while(active){if(done){return null;}else{{return "open";}}}}}
      """;

  public static void main(String... args) {
    runPrettyPrinterDemo();

    SyntaxHighlighter antlrToken = new AntlrTokenCollector();
    EditorUI.show(Texts.START_TEXT, antlrToken);
  }

  private static void runPrettyPrinterDemo() {
    int indentWidth = readIndentWidth();

    System.out.println();
    System.out.println("Pretty-Printer Demo:");
    System.out.println(PrettyPrinter.format(PRETTY_PRINTER_SAMPLE, indentWidth));
  }

  private static int readIndentWidth() {
    System.out.print("Einrueckung pro Stufe (2, 4 oder 8): ");

    var scanner = new Scanner(System.in);
    try {
      if (!scanner.hasNextLine()) {
        System.out.println("Nutze 4 Leerzeichen pro Einrueckstufe.");
        return 4;
      }

      int indentWidth = Integer.parseInt(scanner.nextLine().trim());
      if (indentWidth > 0) {
        return indentWidth;
      }
    } catch (NumberFormatException ignored) {
      // The demo should still run when the input is empty or not a number.
    }

    System.out.println("Nutze 4 Leerzeichen pro Einrueckstufe.");
    return 4;
  }
}
