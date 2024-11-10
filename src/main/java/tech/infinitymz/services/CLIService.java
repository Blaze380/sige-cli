package tech.infinitymz.services;

import java.io.FileNotFoundException;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import tech.infinitymz.lib.utils.LinePrinter;
import tech.infinitymz.lib.utils.Terminal;

public class CLIService {

    public CLIService() {
        AnsiConsole.systemInstall();
        System.out.println("Seja Bem-vindo ao Sistema de Gestao Academico!");
        initComponents();
    }

    private void initComponents() {
        loadCommands();
        CommandLine();
    }

    private void CommandLine() {
        String cmd = null;
        while (true) {
            cmd = Terminal.readLine();
            SyntaxCheckerService.eval(cmd);
        }

    }

    private void loadCommands() {
        try {
            SyntaxCheckerService.loadCommands();
        } catch (FileNotFoundException e) {
            LinePrinter.print(Ansi.ansi().bgYellow().fgBrightBlack().a(" AVISO!").reset());
            LinePrinter.println(
                    " Os comando não foram carregados, qualquer comando que for inserido incorretamente irá danificar o sistema!");
        }
    }

}
