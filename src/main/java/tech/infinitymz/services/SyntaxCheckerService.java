package tech.infinitymz.services;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tech.infinitymz.lib.utils.LinePrinter;

public class SyntaxCheckerService {
    private static OperationsService operations;

    /**
     * Carrega os comandos na memória
     *
     * @throws FileNotFoundException - se não existir nenhum arquivo de comandos
     *                               salvo
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
        // LinePrinter.println("Evaluating: " + cmd);
        if (checkQuotes(cmd) % 2 != 0) {
            LinePrinter.cmdNotFoundMsg(cmd);
            return;
        }

        List<String> commandParts = getCommands(cmd);
        /**
         * Caso seja igual a nul o objeto, quer dizer que o usuário simplesmente deu
         * enter sem inserir nenhum comando
         */
        if (commandParts == null)
            return;

        // LinePrinter.println("Command parts: " + commandParts.toString());
        if (commandParts.size() == 0) {
            if (cmd.equals(""))
                return;
            if (cmd.length() > 0) {
                LinePrinter.cmdNotFoundMsg(cmd);
                return;
            }
            throw new IllegalStateException(
                    "This is an unexpected error because the syntax checker didn't get any command on " + cmd
                            + " and it can be an unknown symbol");
        }
        /**
         * Divide o prefixo e os argumentos do comando
         */
        String prefix = commandParts.get(0);
        String[] args = commandParts.subList(1, commandParts.size()).toArray(new String[0]);
        for (int i = 0; i < args.length; i++)
            args[i] = args[i].toLowerCase();

        if (commandParts.size() > 0) {
            executeCommand(prefix, args);
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

    /**
     * Permite a execução da operação pretendida pelo comando, retorna uma mensagem
     * de erro caso o comando não pertence ao shell
     *
     * @param cmd  - prefixo do comando
     * @param args - argumentos do comando
     */
    private static void executeCommand(String cmd, String[] args) {

        if (operations == null)
            throw new IllegalStateException(
                    "It can not execute operations because the commands was not loaded. user loadCommands() methods first");
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

            case "help":
                operations.printAllCommands(cmd, args);
                break;
            case "exit":
                operations.exitProgram(cmd, args);
                break;
            case "clear":
                operations.clearTerminal(cmd, args);
                break;
            case "test":
                operations.testApp(cmd, args);
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
        // LinePrinter.println("Comando executado");
    }

}
