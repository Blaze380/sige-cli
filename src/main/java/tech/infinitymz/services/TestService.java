package tech.infinitymz.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.fusesource.jansi.Ansi.Color;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import tech.infinitymz.App;
import tech.infinitymz.lib.utils.FileBuffer;
import tech.infinitymz.lib.utils.LinePrinter;
import tech.infinitymz.lib.utils.Terminal;

public class TestService {
    private static List<String> commands;
    private static long commandsUsed;
    private static long totalCommands;
    private static Exception e = null;

    public static class Logs {
        public static int warns;
        public static int errors;
        public static int infos;
        public static String command;
        public static String log;
        public static boolean hasStartedTesting;

    }

    public static void test() {
        App.isTestMode = true;
        Logs.hasStartedTesting = true;
        LinePrinter.isLoading = true;
        new Thread(() -> {
            rebuildTerminal();
            Terminal.clear();
            LinePrinter.info("Terminal carregado");
            readFile();
            LinePrinter.info("Comandos carregado");
            insertCommands();
        }).start();
        try {

            executeCommands();
        } catch (Exception ee) {
            e = ee;
            System.out.println(e.getCause());
            e.printStackTrace();
            System.exit(0);
        }
        App.isTestMode = false;
        Logs.hasStartedTesting = false;
    }

    private static void rebuildTerminal() {
        try {
            Terminal.rebuildTerminalInTestMode();
        } catch (IOException e) {
            LinePrinter.error("Algo deu errado");
            e.printStackTrace();
            System.exit(0);
        }
    }

    private static void insertCommands() {
        totalCommands = commands.size();
        LinePrinter.isLoading = false;
        LinePrinter.info("Inserindo");
        for (int i = 0; i < totalCommands; i++)
            try {
                if (e != null) {
                    throw new IllegalStateException("Hummmmm");
                }
                Logs.command = commands.removeFirst();
                // LinePrinter.println("Comando inserido: " + Logs.command);
                Terminal.simulateCommand(Logs.command);
                ++commandsUsed;
                if (!App.canPrintInTestMode)
                    loadingBar();
                while (LinePrinter.isLoading) {
                    LinePrinter.sleep(300);
                    // LinePrinter.println("No Loop: " + i);
                }
                // LinePrinter.println("Executado comando novamente");
            } catch (IOException | InterruptedException ee) {
                e = ee;
                LinePrinter.error("Algo deu errado\n");
                e.printStackTrace();
                System.exit(0);
            }

    }

    private static void readFile() {

        try {
            commands = FileBuffer.readCommands();
        } catch (FileNotFoundException e) {
            LinePrinter.error("Arquivo nao encontrado!");
            System.exit(0);
        }
        LinePrinter.isLoading = false;
    }

    private static void executeCommands() {
        while (commands == null)
            LinePrinter.loadingLoop("Carregando");

        String cmd = null;
        for (int i = 0; i < totalCommands; i++) {
            cmd = Terminal.readLine();
            LinePrinter.isLoading = true;
            // LinePrinter.println("Lendo comando");
            SyntaxCheckerService.eval(cmd);
            // LinePrinter.println("Lido!!");
            LinePrinter.isLoading = false;
        }
    }

    public static void loadingBar() {
        final double progressPercentage = ((double) commandsUsed / (double) totalCommands * 100);
        final int bars = (int) progressPercentage / 5;
        String progress = "";
        String blank = "";
        for (int i = 0; i < 20; i++)
            if (i < bars)
                progress += "#";
            else
                blank += " ";
        progress = LinePrinter.getBgColored(progress, Color.MAGENTA) + blank;
        String cmds = "\rCommandos: " + totalCommands + "  Executados: " + commandsUsed + "\n";
        String logs = "Erros: " + LinePrinter.getColored(Logs.errors + "", Color.RED) + "  Avisos: "
                + LinePrinter.getColored(Logs.warns + "", Color.YELLOW) + " Certos: "
                + LinePrinter.getColored(Logs.infos + "", Color.GREEN) + "\n\n";

        String executedCmd = "Comando: " + LinePrinter.getColored(Logs.command, Color.MAGENTA) +
                "\n" + Logs.log + "\n";
        Terminal.clear();
        LinePrinter.print(cmds + logs + executedCmd + "Progresso: [" + progress + "] "
                + (progressPercentage > 0 ? "0" : "") + new DecimalFormat("#.00").format(progressPercentage) + " %");
    }
}
