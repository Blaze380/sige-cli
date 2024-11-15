package tech.infinitymz.lib.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;
import org.jline.reader.LineReader.SuggestionType;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.TerminalBuilder;

import tech.infinitymz.App;

public class Terminal {

    @Deprecated
    private static Scanner scanner;
    private static org.jline.terminal.Terminal terminal;
    private static org.jline.reader.LineReader reader;
    private static PipedOutputStream commandWriter;

    public static void create() {
        try {
            buildTerminal();
            Terminal.clear();
        } catch (IOException e) {
            LinePrinter.println(Ansi.ansi().fgRed().a("ERRO! ").reset().a("Erro ao criar o terminal.\nFechando..."));
            System.exit(1);
        }
    }

    public static void simulateCommand(String cmd) throws IOException, InterruptedException {
        Thread.sleep(10);
        commandWriter.write((cmd + "\n").getBytes());
        commandWriter.flush();
    }

    public static void rebuildTerminalInTestMode() throws IOException {
        // TestOutputStream
        PipedInputStream testInput = new PipedInputStream();
        commandWriter = new PipedOutputStream(testInput);
        reader = LineReaderBuilder
                .builder()
                .terminal(TerminalBuilder
                        .builder()
                        .streams(testInput, new TestOutputStream()).build())
                .build();
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

    public static String readGeneric(String m) {
        return reader.readLine(m);
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
            LinePrinter.error("Para sair use o" + LinePrinter.getColored("exit", Color.MAGENTA) + " ou "
                    + LinePrinter.getColored("help", Color.MAGENTA) + " para mais informacoes");
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
            LinePrinter.error("Erro ao limpar o console.");
        }
    }

    public static String readString() {
        final String str = scanner.nextLine();
        return str;
    }

    @Deprecated
    public static double readDouble() {
        final double dbl = scanner.nextDouble();
        return dbl;
    }

    @Deprecated
    public static double readInteger() {
        final int inT = scanner.nextInt();
        return inT;
    }

    static class TestOutputStream extends OutputStream {

        @Override
        public void write(int b) throws IOException {

        }

        @Override
        public void write(byte[] b) throws IOException {

        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {

        }

    }
}
