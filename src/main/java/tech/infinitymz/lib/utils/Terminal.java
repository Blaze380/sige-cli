package tech.infinitymz.lib.utils;

import java.io.IOException;
import java.util.Scanner;

import org.fusesource.jansi.Ansi;
import org.jline.reader.LineReader.SuggestionType;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.TerminalBuilder;

public class Terminal {

    @Deprecated
    private static Scanner scanner;
    private static org.jline.terminal.Terminal terminal;
    private static org.jline.reader.LineReader reader;

    public static void create() {
        try {
            buildTerminal();
            Terminal.clear();
        } catch (IOException e) {
            LinePrinter.println(Ansi.ansi().fgRed().a("ERRO! ").reset().a("Erro ao criar o terminal.\nFechando..."));
            System.exit(1);
        }
    }

    private static void buildTerminal() throws IOException {

        scanner = new Scanner(System.in);
        terminal = TerminalBuilder.builder().system(true).build();

        reader = LineReaderBuilder
                .builder()
                .terminal(terminal)
                .build();

        reader.setAutosuggestion(SuggestionType.HISTORY);

    }

    public static String readLine() {
        if (terminal == null || reader == null || scanner == null)
            throw new IllegalStateException("Terminal não foi inicializado.");
        try {
            return reader.readLine(
                    Ansi
                            .ansi()
                            .fg(Ansi.Color.GREEN)
                            .a("Creator@zsh: ")
                            .fgBrightMagenta()
                            .a("~/App/ustm")
                            .reset()
                            .a(" $ ")
                            .toString());

        } catch (UserInterruptException e) {
            LinePrinter.println(Ansi
                    .ansi()
                    .fgBrightRed()
                    .a("ERRO! ")
                    .reset()
                    .a("Para sair use o ")
                    .fgBrightMagenta()
                    .a("exit")
                    .reset()
                    .a(" ou ")
                    .fgBrightMagenta()
                    .a("help")
                    .reset()
                    .a(" para mais informações"));
        }
        return null;
    }

    public static void clear() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win"))
                new ProcessBuilder("cmd", "/c", "cls")
                        .inheritIO()
                        .start()
                        .waitFor();
            else
                new ProcessBuilder("clear")
                        .inheritIO()
                        .start()
                        .waitFor();

        } catch (Exception e) {
            LinePrinter.println(Ansi.ansi().fgRed().a("ERRO! ").reset().a("Erro ao limpar o console."));
        }
    }

    public static String readString() {
        final String str = scanner.nextLine();
        return str;
    }

    public static double readDouble() {
        final double dbl = scanner.nextDouble();
        return dbl;
    }

    public static double readInteger() {
        final int inT = scanner.nextInt();
        return inT;
    }
}
