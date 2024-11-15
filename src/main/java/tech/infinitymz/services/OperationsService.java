package tech.infinitymz.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;

import org.fusesource.jansi.Ansi.Color;
import org.fusesource.jansi.AnsiConsole;

import com.google.gson.Gson;

import lombok.Getter;
import tech.infinitymz.App;
import tech.infinitymz.enums.SubjectPriority;
import tech.infinitymz.lib.collections.HashTable;
import tech.infinitymz.lib.utils.FileBuffer;
import tech.infinitymz.lib.utils.LinePrinter;
import tech.infinitymz.lib.utils.Terminal;
import tech.infinitymz.models.Command;
import tech.infinitymz.models.Command.Argument;
import tech.infinitymz.models.Course;
import tech.infinitymz.models.CurriculumPlan;
import tech.infinitymz.models.Subject;
import tech.infinitymz.models.Topics;

@SuppressWarnings("unused")
public class OperationsService {
    private HashTable<String, Course> courses;
    @Getter
    private HashTable<String, Command> commands;

    public OperationsService() throws FileNotFoundException {
        this.commands = new HashTable<>();
        this.courses = new HashTable<>();
        loadCmd();
    }

    /**
     * Imprimi uma explicação detalhada acerca do determinado comando
     *
     * @param cmd  - prefixo do comando
     * @param args - argumentos, espera-se que para mostrar a explicação tenha como
     *             argumento somente o "--help"
     * @return true caso seja um comando de ajuda
     */
    private boolean isHelpOption(String cmd, String args[]) {
        if (args.length != 1)
            return false;
        if (args[0].equals("--help")) {
            var command = commands.find(cmd).getValue();
            String shellCommand = "Uso: " + command.getPrefix();

            if (command.getArgs().length > 0)
                for (Argument arg : command.getArgs())
                    shellCommand += " [" + arg.getName() + "]";

            shellCommand += "\n\n" + command.getDescription() + "\n\n"
                    + (command.getArgs().length > 0 ? "Argumentos:" : "")
                    + "\n";

            if (command.getArgs().length > 0)
                for (Argument arg : command.getArgs())
                    shellCommand += "\n - " + arg.getName() + " \t " + arg.getDescription();

            LinePrinter.println(shellCommand);
            return true;
        }
        return false;
    }

    /**
     * Verifica se o número de argumentos passados pelo comando, correspondem ao
     * número de argumentos necessários para a execução do comando
     *
     * @param cmd       - prefixo
     * @param argLength - número de args
     * @return true caso corresponda o número de argumentos
     */
    private boolean checkCommandArgsLength(String cmd, int argLength) {
        Command command = commands.find(cmd).getValue();

        boolean isTrue = (command.getArgs().length == argLength);
        if (!isTrue)
            LinePrinter.syntaxError(cmd,
                    "Falta de argumentos: Espera-se ter (" + command.getArgs().length + ") argumentos");
        return isTrue;

    }

    /**
     * Verifica se o número de argumentos passados pelo comando, correspondem ao
     * número de argumentos necessários para a execução do comando
     *
     * @param cmd            - prefixo
     * @param argLength      - número de args
     * @param expectedLength - uma expetativa de tamanho para comandos de multi
     *                       interação
     * @return true caso corresponda o número de argumentos
     */
    private boolean checkCommandArgsLength(String cmd, int argLength, int expectedLength) {
        final Command command = commands.find(cmd).getValue();
        if (argLength < 1)
            LinePrinter.syntaxError(cmd, "Falta de argumentos: Espera-se ter (" + expectedLength + ") argumentos");

        if (command.getArgs().length > expectedLength)
            return argLength == expectedLength;
        LinePrinter.syntaxError(cmd, "Falta de argumentos: Espera-se ter (" + expectedLength + ") argumentos");
        return false;

    }

    /**
     * Carrega os comandos no disco e aloca na memória
     *
     * @throws FileNotFoundException - Caso não tenha nenhum arquivo
     */
    private void loadCmd() throws FileNotFoundException {
        File file = FileBuffer.createDirectoryPath(FileBuffer.RESOURCE_PATH);
        String content = FileBuffer.readFile(file, FileBuffer.COMMANDS_FILE);

        final Command[] cmd = (Command[]) new Gson().fromJson(content, Command[].class);
        for (Command command : cmd)
            commands.add(command.getPrefix(), command);
    }

    /**
     * Verifica se o argumento do tipo string faz parte dos limites concebidos pelo
     * atributo
     */
    private boolean checkArgLength(String argName, String cmd, String args) {
        final boolean isRight = args.length() >= 1 && args.length() <= 60;
        if (!isRight)
            LinePrinter.syntaxError(cmd,
                    argName + " deve ter no maximo 60 caracteres: " + LinePrinter.getColored(argName, Color.RED));
        return isRight;
    }

    /**
     * Verifica se o argumento do tipo decimal faz parte dos limites concebidos pelo
     * atributo
     */
    private boolean checkArgDoubleBound(String argName, String cmd, double arg, double start, double end) {
        final boolean isRight = arg >= start && arg <= end;
        if (!isRight)
            LinePrinter.syntaxError(cmd,
                    argName + " deve estar entre " + start + " e " + end + ": "
                            + LinePrinter.getColored(argName, Color.RED));
        return isRight;
    }

    /**
     * Verifica se o argumento do tipo inteiro faz parte dos limites concebidos pelo
     * atributo
     */
    private boolean checkArgIntBound(String argName, String cmd, int arg, int start, int end) {
        final boolean isRight = arg >= start && arg <= end;
        if (!isRight)
            LinePrinter.syntaxError(cmd,
                    argName + " deve estar entre " + start + " e " + end + ": "
                            + LinePrinter.getColored(argName, Color.RED));
        return isRight;
    }

    /**
     * Operação de criação de curso
     *
     * @param cmd
     * @param args
     */
    public void createCourse(String cmd, String[] args) {
        if (isHelpOption(cmd, args))
            return;
        if (!checkCommandArgsLength(cmd, args.length))
            return;
        if (!checkArgLength("course", cmd, args[0]))
            return;

        if (hasCourse(args[0], true, false))
            return;

        courses.add(args[0], new Course(args[0], new CurriculumPlan()));
        LinePrinter.info("Curso criado com sucesso");
    }

    /**
     * Remove curso
     *
     * @param cmd
     * @param args
     */
    public void removeCourse(String cmd, String[] args) {
        if (isHelpOption(cmd, args))
            return;

        if (!checkCommandArgsLength(cmd, args.length))
            return;
        if (!checkArgLength("course", cmd, args[0]))
            return;

        if (!hasCourse(args[0], false, true))
            return;
        courses.remove(args[0]);
        LinePrinter.info("Curso removido com sucesso");
    }

    /**
     * Cria disciplina
     *
     * @param cmd  - prefixo do comando
     * @param args - argumentos do comando
     */
    public void createSubject(String cmd, String args[]) {
        if (!checkSubjectArgs(cmd, args))
            return;

        final String course = args[2];
        final int priority = Integer.parseInt(args[1]);
        final int semester = Integer.parseInt(args[0]);

        SubjectPriority sPriority = null;
        if (priority == SubjectPriority.OPTIONAL.getPriority())
            sPriority = SubjectPriority.OPTIONAL;
        else if (priority == SubjectPriority.OBLIGATORY.getPriority())
            sPriority = SubjectPriority.OBLIGATORY;

        args = getUserArgs("[numero-creditos] [disciplina]");

        if (args.length > 2 || args.length <= 0) {
            LinePrinter.syntaxError(cmd);
            return;
        }
        double credits = 0;
        String subject = null;
        try {
            credits = Double.parseDouble(args[0]);
            subject = args[1];

            if (!checkArgLength("disciplina", cmd, subject))
                return;
            if (hasSubject(subject, true, false))
                return;

        } catch (NumberFormatException e) {
            LinePrinter.syntaxError(cmd, "O numero de creditos deve estar no intervalo entre 0.5 e 30");
            return;
        } catch (ArrayIndexOutOfBoundsException e) {
            LinePrinter.syntaxError(cmd,
                    "Falta de argumentos: " + args[0] + " espera-se ter (2) argumentos");
            return;
        }
        // preparar o array para receber 4 tópicos
        final Topics[] topics = new Topics[4];
        boolean duplicatedTopic = false;

        /**
         * Adiciona os 4 tópicos
         */
        for (int i = 0; i < 4; i++) {

            args = getUserArgs("[topico" + (i + 1) + "]");
            if (args.length > 1 || args.length <= 0) {
                LinePrinter.syntaxError(cmd);
                return;
            }
            /**
             * Verifica se o novo tópico já existe nos tópicos já adicionados
             */
            for (Topics j : topics) {
                if (j == null)
                    continue;
                if (j.getTopic() != null)
                    if (j.getTopic().equals(args[0])) {
                        LinePrinter.error("Topico duplicado");
                        return;
                    }
            }
            if (!duplicatedTopic) {
                if (topics[i] == null)
                    topics[i] = new Topics();
                if (args.length == 0)
                    duplicatedTopic = true;
                else {
                    topics[i].setTopic(args[0]);
                    break;

                }
            }
        }
        courses.find(course).getValue().getCurriculumPlan().getSubjects().add(subject,
                new Subject(subject, credits, sPriority, semester, topics));
        LinePrinter.info("Disciplina criada com sucesso");

    }

    private String[] getUserArgs(String args) {
        if (App.isTestMode)
            LinePrinter.isLoading = false;
        final String cmd = Terminal.readGeneric(LinePrinter.getColored("- " + args + " > ", Color.MAGENTA));

        if (App.isTestMode)
            LinePrinter.isLoading = true;
        final List<String> cmds = SyntaxCheckerService.getCommands(cmd);
        final String[] array = new String[cmds.size()];
        for (int i = 0; i < array.length; i++)
            array[i] = cmds.get(i);
        return array;

    }

    private boolean hasCourse(String course, boolean printableIfTrue, boolean printableIfFalse) {
        final boolean hasCourse = (courses.find(course) != null);
        if (hasCourse) {
            if (printableIfTrue)
                LinePrinter.warn("O Curso " + LinePrinter.getColored(course, Color.YELLOW) + " ja existe");
        } else {
            if (printableIfFalse)
                LinePrinter.error("O Curso " + LinePrinter.getColored(course, Color.RED) + " nao existe");
        }
        return hasCourse;
    }

    private boolean hasCourse(String course) {
        final boolean hasCourse = (courses.find(course) != null);
        if (hasCourse) {
            LinePrinter.warn("O Curso " + LinePrinter.getColored(course, Color.YELLOW) + " ja existe");
        } else if (!hasCourse) {
            LinePrinter.error("O Curso " + LinePrinter.getColored(course, Color.RED) + " nao existe");
        }

        return hasCourse;
    }

    private boolean hasSubject(String subject, boolean printableIfTrue, boolean printableIfFalse) {
        boolean hasSubject = false;
        for (Course course : courses.getValues()) {
            if (course.getCurriculumPlan().getSubjects().find(subject) != null) {
                hasSubject = true;
                break;
            }
        }
        if (hasSubject) {
            if (printableIfTrue)
                LinePrinter.error("A disciplina " + LinePrinter.getColored(subject, Color.RED) + " existe");
        } else {
            if (printableIfFalse)
                LinePrinter.error("A disciplina " + LinePrinter.getColored(subject, Color.RED) + " nao existe");
        }
        return hasSubject;
    }

    public void searchSubject(String cmd, String[] args) {
        if (isHelpOption(cmd, args))
            return;

        if (!checkCommandArgsLength(cmd, args.length))
            return;
        if (!checkArgLength("disciplina", cmd, args[0]))
            return;
        boolean hasAtLeast1Subject = false;
        for (Course course : courses.getValues())
            for (Subject subject : course.getCurriculumPlan().getSubjects().getValues())
                if (subject.getName().equals(args[0])) {
                    if (!hasAtLeast1Subject) {
                        hasAtLeast1Subject = true;
                        LinePrinter.println("Semestre\t Prioridade \t Curso");
                    }
                    LinePrinter
                            .println(subject.getSemester() + "\t " + subject.getPriority() + "\t " + course.getName());
                    for (Topics topic : subject.getTopics()) {

                        LinePrinter.println("Topico - " + topic.getTopic());
                        LinePrinter.println("--------------------------------");
                    }

                }
        if (!hasAtLeast1Subject)
            LinePrinter.error("A disciplina " + LinePrinter.getColored(args[0], Color.RED) + " existe");

    }

    public void searchCurriculumPlan(String cmd, String[] args) {
        if (isHelpOption(cmd, args))
            return;

        if (!checkCommandArgsLength(cmd, args.length))
            return;
        if (!checkArgLength("curso", cmd, args[0]))
            return;
        if (!hasCourse(args[0]))
            return;
        for (Course course : courses.getValues())
            if (course.getName().equals(args[0])) {
                LinePrinter.println(LinePrinter.getColored("PLANO CURRICULAR - " + args[0], Color.MAGENTA));

                if (course.getCurriculumPlan().getSubjects().getValues() == null
                        || course.getCurriculumPlan().getSubjects().getValues().size() < 1) {
                    LinePrinter.error("O curso " + LinePrinter.getColored(args[0], Color.RED)
                            + " ainda nao possui disciplinas em seu plano curricular.");
                    return;
                }
                LinePrinter.println("Semestre\t Prioridade\t Numero creditos\t Curso");
                for (Subject subject : course.getCurriculumPlan().getSubjects().getValues()) {
                    LinePrinter
                            .println(subject.getSemester() + "\t " + subject.getPriority() + "\t "
                                    + subject.getNumberOfCredits() + "\t " + course.getName());
                }
                break;
            }

    }

    public void removeSubjectFromCurriculumPlan(String cmd, String[] args) {
        if (isHelpOption(cmd, args))
            return;
        if (!checkCommandArgsLength(cmd, args.length, 1))
            return;
        final String course = args[0];
        args = getUserArgs("[disciplina]");

        if (!checkCommandArgsLength(cmd, args.length, 1))
            return;
        final String subject = args[0];

        if (!checkArgLength("curso", cmd, course))
            return;

        if (!checkArgLength("disciplina", cmd, subject))
            return;
        try {

            courses.getValues().forEach(c -> {
                if (c.getName().equals(course))
                    c.getCurriculumPlan().getSubjects().remove(subject);
            });
        } catch (NoSuchElementException e) {
            LinePrinter.error("O plano curricular do curso " + LinePrinter.getColored(course, Color.RED)
                    + " nao possui a disciplina " + LinePrinter.getColored(subject, Color.RED));
        }
    }

    /**
     * Operação de inserção de disciplina em um plano curricular
     *
     * @param cmd
     * @param args
     */
    public void insertSubjectInCurriculumPlan(String cmd, String[] args) {
        if (!checkSubjectArgs(cmd, args))
            return;

        final String courseName = args[2];
        final int priority = Integer.parseInt(args[1]);
        final int semester = Integer.parseInt(args[0]);

        final SubjectPriority sPriority = priority == SubjectPriority.OPTIONAL.getPriority() ? SubjectPriority.OPTIONAL
                : SubjectPriority.OBLIGATORY;

        args = getUserArgs("[disciplina]");

        if (args.length > 1) {
            LinePrinter.syntaxError(cmd);
            return;
        }
        final String subjectName = args[0];
        Course course = courses.find(courseName).getValue();
        Subject subject = null;

        for (Course c : courses.getValues()) {
            subject = c.getCurriculumPlan().getSubjects().find(subjectName).getValue();
            if (subject != null)
                break;
        }
        course.getCurriculumPlan().getSubjects().add(subjectName, subject);
        LinePrinter.info("Disciplina " + subjectName + " adicionada ao curso " + courseName + " com sucesso");
    }

    /**
     * Operação de pesquisa de disciplinas associadas a um determinado tópico
     */
    public void searchSubjectByTopic(String cmd, String[] args) {
        if (isHelpOption(cmd, args))
            return;

        if (!checkCommandArgsLength(cmd, args.length))
            return;
        if (!checkArgLength("topico", cmd, args[0]))
            return;
        final String topic = args[0];
        boolean doesExistAtLeast1Topic = false;
        String printer = "Numero de creditos \t Disciplina\n";
        // List<Subject> subjects = this.subjects.getValues();
        for (Course course : courses.getValues())
            for (Subject subject : course.getCurriculumPlan().getSubjects().getValues())
                for (Topics topic1 : subject.getTopics())
                    if (topic1 != null)
                        if (topic1.getTopic().equals(topic)) {
                            doesExistAtLeast1Topic = true;
                            printer += subject.getNumberOfCredits() + "\t " + subject.getName() + "\n";
                        }

        if (doesExistAtLeast1Topic) {
            LinePrinter.println(printer);
        } else {
            LinePrinter.error(
                    "Nao existe nenhuma disciplina associada ao topico "
                            + LinePrinter.getColored(topic, Color.RED));

        }
    }

    /**
     * Imprime todos os comandos disponíveis
     */
    public void printAllCommands(String cmd, String... args) {
        if (isTestMode(cmd))
            return;
        if (isHelpOption(cmd, args))
            return;
        if (!checkCommandArgsLength(cmd, args.length))
            return;
        String shellCommands = "";
        final List<Command> keys = commands.getValues();
        for (Command command : keys) {

            shellCommands += LinePrinter.getColored(command.getPrefix(), Color.MAGENTA);
            for (Argument arg : command.getArgs())
                shellCommands += " [" + arg.getName() + "]";
            shellCommands += "\n";
        }
        LinePrinter
                .println("Creator Shell, Version 0.1.7-alpha\nTodos os comandos shell disponiveis\n\n"
                        + shellCommands);

    }

    public void exitProgram(String cmd, String... args) {
        if (isTestMode(cmd))
            return;
        if (isHelpOption(cmd, args))
            return;
        if (!checkCommandArgsLength(cmd, args.length))
            return;
        LinePrinter.info("Fechando o terminal...");
        AnsiConsole.systemUninstall();
        System.exit(0);
    }

    public void clearTerminal(String cmd, String[] args) {
        if (isTestMode(cmd))
            return;
        if (isHelpOption(cmd, args))
            return;
        if (!checkCommandArgsLength(cmd, args.length))
            return;
        Terminal.clear();
    }

    private boolean checkSubjectArgs(String cmd, String args[]) {
        if (isHelpOption(cmd, args))
            return false;

        if (!checkCommandArgsLength(cmd, args.length, 3))
            return false;

        final String course = args[2];
        int priority = 0;
        int semester = 0;
        final String sem = args[0];

        try {
            priority = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            LinePrinter.syntaxError(cmd, "Argumento invalido: " + LinePrinter.getColored(priority + "", Color.RED));
            return false;
        }

        /** */
        try {
            semester = Integer.parseInt(sem);
        } catch (NumberFormatException e) {
            LinePrinter.syntaxError(cmd, "Argumento invalido: " + LinePrinter.getColored(sem, Color.RED));
            return false;
        }

        if (!checkArgLength("course", cmd, course))
            return false;
        else if (!checkArgIntBound("semester", cmd, semester, 1, 10))
            return false;
        else if (!checkArgIntBound("priority", cmd, priority, 0, 1))
            return false;

        if (!hasCourse(course, false, true))
            return false;

        SubjectPriority sPriority = null;

        try {
            if (priority == SubjectPriority.OPTIONAL.getPriority())
                sPriority = SubjectPriority.OPTIONAL;
            else if (priority == SubjectPriority.OBLIGATORY.getPriority())
                sPriority = SubjectPriority.OBLIGATORY;
            else
                throw new IllegalArgumentException("Erro: Valor da prioridade da disciplina desconhecido");
        } catch (IllegalArgumentException e) {
            LinePrinter.syntaxError(cmd,
                    "O valor " + LinePrinter.getColored(priority + "", Color.RED) + " e invalido!");
            return false;
        }
        return true;
    }

    public void testApp(String cmd, String args[]) {
        if (isTestMode(cmd))
            return;
        if (isHelpOption(cmd, args))
            return;
        if (!checkCommandArgsLength(cmd, args.length))
            return;
        LinePrinter.info("Adicione o arquivo na no caminho ./resource/test_file.txt");
        args = getUserArgs("Deseja visualizar em modo compacto?(s/n)");
        if (args.length > 1 || args == null || args.length <= 0) {
            LinePrinter.error("Resposta incorreta: respostas possiveis sao s(sim) e n(nao) ");
            return;
        }
        switch (args[0].toLowerCase()) {
            case "s":
                App.canPrintInTestMode = false;
                break;
            case "n":
                App.canPrintInTestMode = true;
                break;
            default:
                LinePrinter.error("Opcao invalida");
                return;
        }
        HashTable<String, Course> c = this.courses;
        this.courses = new HashTable<>();
        TestService.test();
        courses = c;
    }

    private static synchronized boolean isTestMode(String cmd) {
        if (App.isTestMode) {
            LinePrinter.error("Nao pode usar o comando " + cmd + " em modo de testes");
            return true;
        }
        return false;
    }
}
// ?
