package tech.infinitymz.services;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import tech.infinitymz.lib.utils.LinePrinter;
import tech.infinitymz.lib.utils.Terminal;

public class SyntaxCheckerService {
    private static OperationsService operations;

    /**
     * Carrega os comandos na memória
     *
     * @throws FileNotFoundException
     */
    public static void loadCommands() throws FileNotFoundException {
        if (operations == null)
            operations = new OperationsService();
    }

    /**
     * Método publico que dá o início de desestruturação, identificação e execução
     * dos comandos
     *
     * @param cmd
     */
    public static void eval(String cmd) {

        if (checkQuotes(cmd) % 2 != 0) {
            LinePrinter.cmdNotFoundMsg(cmd);
            return;
        }

        List<String> commandParts = getCommands(cmd);
        if (commandParts == null)
            return;
        if (commandParts.size() == 1)
            checkSingleCommand(commandParts.get(0));
        else if (commandParts.size() > 1) {

            String prefix = commandParts.get(0);
            String[] args = commandParts.subList(1, commandParts.size()).toArray(new String[0]);
            executeCommand(prefix, args);
        } else if (commandParts.size() == 0) {
        } else
            LinePrinter.cmdNotFoundMsg(cmd);

    }

    /**
     * Verifica se caso o user tenha usado aspas, elas tenham sido fechas ou não
     *
     * @param cmd
     * @return
     */
    private static int checkQuotes(String cmd) {
        if (cmd == null)
            return 0;
        Matcher quotes = Pattern.compile("\"").matcher(cmd);
        int quoteCount = 0;
        while (quotes.find())
            quoteCount++;
        return quoteCount;
    }

    /**
     * Quebra a cmd String em pedaços para obter o prefixo e seus args
     *
     * @param cmd
     * @return
     */
    public static List<String> getCommands(String cmd) {
        if (cmd == null)
            return null;
        Pattern pattern = Pattern.compile("\"([^\"]*)\"|(\\S+)");
        Matcher matcher = pattern.matcher(cmd);

        List<String> commandParts = new ArrayList<>();
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                commandParts.add(matcher.group(1)); // Argumento entre aspas
            } else {
                commandParts.add(matcher.group(2)); // Palavra separada por espaço
            }
        }
        return commandParts;
    }

    private static void executeCommand(String cmd, String[] args) {

        switch (cmd) {
            case "CC":
                operations.createCourse(cmd, args);
                break;
            case "RC":
                operations.removeCourse(cmd, args);
                break;
            case "CD":
                operations.createSubject(cmd, args);
                break;
            case "PD":
                operations.searchSubject(cmd, args);
                break;
            case "DT":
                operations.searchSubjectByTopic(cmd, args);
                break;
            case "ID":
                operations.insertSubjectInCurriculumPlan(cmd, args);
                break;
            case "RD":
                operations.removeSubjectFromCurriculumPlan(cmd, args);
                break;
            case "PP":
                operations.searchCurriculumPlan(cmd, args);
                break;

            default:
                break;
        }
    }

    /**
     * Verifica se são comandos do sistema e os executa
     *
     * @param cmd
     */
    private static void checkSingleCommand(String cmd) {
        switch (cmd) {
            case "help":
                // TODO coloca o sistema de help
                LinePrinter.info("Ajuda....");
                break;
            case "exit":
                LinePrinter.info("Fechando...");
                AnsiConsole.systemUninstall();
                System.exit(0);
                break;
            case "clear":
                Terminal.clear();
                break;
            default:
                if (operations.getCommands().find(cmd) != null) {
                    LinePrinter.syntaxError(cmd);
                    return;
                }
                if (cmd != null)
                    LinePrinter.cmdNotFoundMsg(cmd);
                break;
        }
    }
}
