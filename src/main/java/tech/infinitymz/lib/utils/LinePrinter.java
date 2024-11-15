package tech.infinitymz.lib.utils;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;

import tech.infinitymz.App;
import tech.infinitymz.services.TestService;
import tech.infinitymz.services.TestService.Logs;

public class LinePrinter {
    public static boolean isLoading = false;

    public static void print(String s) {
        System.out.print(s);
    }

    public static void print(Object o) {
        System.out.print(o);
    }

    public static void println(String s) {
        System.out.println(s);
    }

    public static void println(Object o) {
        System.out.println(o);
    }

    public static void info(String msg) {
        Logs.log = msg = Ansi.ansi().fgBrightBlue().a("INFO: ").reset().a(msg).toString();
        if (Logs.hasStartedTesting)
            ++Logs.infos;
        if (App.canPrintInTestMode)
            println(msg);
    }

    public static void warn(String msg) {
        Logs.log = msg = Ansi.ansi().fgYellow().a("AVISO: ").reset().a(msg).toString();
        if (Logs.hasStartedTesting)
            ++Logs.warns;
        if (App.canPrintInTestMode)
            println(msg);
    }

    public static void error(String msg) {
        Logs.log = msg = Ansi.ansi().fgBrightRed().a("ERRO: ").reset().a(msg).toString();
        if (Logs.hasStartedTesting)
            ++Logs.errors;
        if (App.canPrintInTestMode)
            println(msg);
        ;
    }

    public static String loadingLoop(String m) {
        while (isLoading) {
            System.out.print("\r" + m);
            for (int i = 0; i < 3; i++) {
                System.out.print(".");
                sleep(500);
            }
        }
        return m;
    }

    public static void sleep(int n) {
        try {
            Thread.sleep(n);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static String getColored(String msg, Color c) {
        return Ansi.ansi().fg(c).a(msg).reset().toString();
    }

    public static String getBgColored(String msg, Color c) {
        return Ansi.ansi().bg(c).a(msg).reset().toString();
    }

    public static void cmdNotFoundMsg(String cmd) {
        error(Ansi.ansi()
                .a("Comando nao encontrado: " + (cmd != null ? getColored(cmd, Color.MAGENTA) : "")
                        + "! Use o comando ")
                .fgBrightMagenta()
                .a("help")
                .reset()
                .a(" para mais informacoes.").toString());
    }

    public static void syntaxError(String cmd, String reason) {
        error(reason + "! Tente " + getColored(cmd, Color.MAGENTA) + getColored(" --help", Color.MAGENTA)
                + "  ou " + getColored("help", Color.MAGENTA) + " para mais informacoes.");
    }

    public static void syntaxError(String cmd) {
        error(
                "Erro de sintaxe! Tente " + getColored(cmd, Color.MAGENTA) + getColored(" --help", Color.MAGENTA)
                        + "  ou " + getColored("help", Color.MAGENTA) + " para mais informacoes.");
    }

}
