package tech.infinitymz.services;

import java.io.File;
import java.io.FileNotFoundException;

import org.fusesource.jansi.Ansi.Color;

import com.google.gson.Gson;

import lombok.Getter;
import tech.infinitymz.enums.SubjectPriority;
import tech.infinitymz.lib.collections.HashTable;
import tech.infinitymz.lib.utils.FileBuffer;
import tech.infinitymz.lib.utils.LinePrinter;
import tech.infinitymz.lib.utils.Terminal;
import tech.infinitymz.models.Command;
import tech.infinitymz.models.Course;
import tech.infinitymz.models.CurriculumPlan;
import tech.infinitymz.models.Subject;
import tech.infinitymz.models.Topics;

@SuppressWarnings("unused")
public class OperationsService {
    private HashTable<String, Course> courses;
    private HashTable<String, Subject> subjects;
    private HashTable<String, CurriculumPlan> curriculumPlans;
    @Getter
    private HashTable<String, Command> commands;

    public OperationsService() throws FileNotFoundException {
        this.commands = new HashTable<>();
        this.courses = new HashTable<>();
        this.subjects = new HashTable<>();
        this.curriculumPlans = new HashTable<>();
        loadCmd();
    }

    private boolean checkCommandArgsLength(String cmd, int argLength) {
        Command command = commands.find(cmd).getValue();
        boolean isTrue = (command.getArgs().length == argLength);
        if (!isTrue)
            LinePrinter.syntaxError(cmd, "Erro de sintaxe!");
        return isTrue;

    }

    private boolean checkCommandArgsLength(String cmd, int argLength,int expectedLength) {
        Command command = commands.find(cmd).getValue();
        //final boolean isTrue =false;
        if(command.getArgs().length=>expectedLength)
            return argLength==expectedLength;
            LinePrinter.syntaxError(cmd, "Erro de sintaxe!");
        return false;

    }

    private void loadCmd() throws FileNotFoundException {
        File file = FileBuffer.createDirectoryPath(FileBuffer.RESOURCE_PATH);
        String content = FileBuffer.readFile(file, FileBuffer.COMMANDS_FILE);

        final Command[] cmd = (Command[]) new Gson().fromJson(content, Command[].class);
        for (Command command : cmd)
            commands.add(command.getPrefix(), command);
    }

    public void createCourse(String cmd, String[] args) {
        if (!checkCommandArgsLength(cmd, args.length))
            return;

        if (hasCourse(args[0])) {
            LinePrinter.warn("O Curso " + LinePrinter.getColored(args[0], Color.YELLOW) + " ja existe");
            return;
        }
        courses.add(args[0], new Course(args[0], new CurriculumPlan()));
        LinePrinter.info("Curso criado com sucesso");
    }

    public void removeCourse(String cmd, String[] args) {
        if (!checkCommandArgsLength(cmd, args.length))
            return;

        if (!checkCommandArgsLength(cmd, args.length)) {
            LinePrinter.syntaxError(cmd, "Erro de sintaxe!");
            return;
        }
        if (!hasCourse(args[0])) {
            LinePrinter.error("O Curso " + LinePrinter.getColored(args[0], Color.RED) + " nao existe");
            return;
        }
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
        // Verifica se o número de argumentos passados, corresponde ao número de
        // argumento que o comando precisa
        if (!checkCommandArgsLength(cmd, 3))
            return;
        final String course = args[2];
        final String sem = args[0];
        final String priority = args[1];
        final int semester = Integer.parseInt(sem);

        if (course.length() > 60 || (semester < 1 || semester > 10) || priority.length() > 1) {
            LinePrinter.syntaxError(cmd);
            return;
        }

        if (!hasCourse(course)) {
            LinePrinter.error("O Curso " + LinePrinter.getColored(course, Color.RED) + " nao existe");
            return;
        }

        SubjectPriority sPriority = null;

        try {
            sPriority = SubjectPriority.valueOf(priority);
        } catch (IllegalArgumentException e) {
            LinePrinter.syntaxError(cmd,
                    "O valor " + LinePrinter.getColored(priority, Color.RED) + " é inválido!");
            return;
        }

        // Recebe input de número de créditos e disciplina
        args = getUserArgs();

        if (args.length > 2) {
            LinePrinter.syntaxError(cmd);
            return;
        }
        double credits = 0;
        String subject = null;
        try {
            credits = Double.parseDouble(args[0]);
            subject = args[1];
            if (!hasSubject(subject)) {
                LinePrinter.error("A disciplina " + LinePrinter.getColored(subject, Color.RED) + " nao existe");
                return;
            }
        } catch (Exception e) {
            LinePrinter.syntaxError(cmd);
            return;
        }
        // preparar o array para receber 4 tópicos
        final Topics[] topics = new Topics[4];
        boolean duplicatedTopic = false;

        /**
         * Adiciona os 4 tópicos
         */
        for (int i = 1; i <= 4; i++) {
            while (true) {
                args = getUserArgs();
                if (args.length > 1) {
                    LinePrinter.syntaxError(cmd);
                    return;
                }
                /**
                 * Verifica se o novo tópico já existe nos tópicos já adicionados
                 */
                for (Topics j : topics) {
                    if (j.getTopic().equals(args[0])) {
                        LinePrinter.error("Topico duplicado");
                        duplicatedTopic = true;
                        break;
                    }
                }
                if (!duplicatedTopic) {
                    topics[i].setTopic(args[0]);
                    break;
                }
            }
        }
        subjects.add(subject, new Subject(subject, credits, sPriority, semester, topics));

        LinePrinter.info("Disciplina criada com sucesso");

    }

    private String[] getUserArgs() {
        LinePrinter.print(LinePrinter.getColored("- > ", Color.MAGENTA));
        return (String[]) SyntaxCheckerService.getCommands(Terminal.readString()).toArray();
    }

    private boolean hasCourse(String course) {
        return (courses.find(course) != null);
    }

    private boolean hasSubject(String subject) {
        return (subjects.find(subject) != null);
    }

    private boolean hasCurriculumPlan(String curriculumPlan) {
        return (curriculumPlans.find(curriculumPlan) != null);
    }

    public void searchSubject(String cmd, String[] args) {
        LinePrinter.syntaxError(cmd, "Esta operacao ainda nao foi implementada, e so esperar");
    }

    public void searchCurriculumPlan(String cmd, String[] args) {
        LinePrinter.syntaxError(cmd, "Esta operacao ainda nao foi implementada, e so esperar");
    }

    public void removeSubjectFromCurriculumPlan(String cmd, String[] args) {
        LinePrinter.syntaxError(cmd, "Esta operacao ainda nao foi implementada, e so esperar");
    }

    public void insertSubjectInCurriculumPlan(String cmd, String[] args) {
        LinePrinter.syntaxError(cmd, "Esta operacao ainda nao foi implementada, e so esperar");
    }

    public void searchSubjectByTopic(String cmd, String[] args) {
        LinePrinter.syntaxError(cmd, "Esta operacao ainda nao foi implementada, e so esperar");
    }

}
