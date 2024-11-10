package tech.infinitymz.lib.utils;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;

public class LinePrinter {
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
        println(Ansi.ansi().fgBrightBlue().a("INFO: ").reset().a(msg).toString());
    }

    public static void warn(String msg) {
        println(Ansi.ansi().fgYellow().a("AVISO: ").reset().a(msg).toString());
    }

    public static void error(String msg) {
        println(Ansi.ansi().fgBrightRed().a("ERRO: ").reset().a(msg).toString());
        ;
    }

    public static String getColored(String msg, Color c) {
        return Ansi.ansi().fg(c).a(msg).reset().toString();
    }

    public static void cmdNotFoundMsg(String cmd) {
        error(Ansi.ansi()
                .a(" Comando nao encontrado: " + (cmd == null ? getColored(cmd, Color.MAGENTA) : "")
                        + "\nUse o comando ")
                .fgBrightMagenta()
                .a("help")
                .reset()
                .a(" para mais informacoes.").toString());
    }

    public static void syntaxError(String cmd, String reason) {
        error(reason + "\nTente " + getColored(cmd, Color.MAGENTA) + getColored(" --help", Color.MAGENTA)
                + "  ou " + getColored("help", Color.MAGENTA) + " para mais informacoes.");
    }

    public static void syntaxError(String cmd) {
        error(
                "Erro de sintaxe!\nTente " + getColored(cmd, Color.MAGENTA) + getColored(" --help", Color.MAGENTA)
                        + "  ou " + getColored("help", Color.MAGENTA) + " para mais informacoes.");
    }

}
