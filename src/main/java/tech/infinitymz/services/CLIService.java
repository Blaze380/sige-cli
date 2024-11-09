package tech.infinitymz.services;

import java.io.File;

import com.google.gson.Gson;

import tech.infinitymz.lib.collections.LinkedList;
import tech.infinitymz.lib.utils.FileBuffer;
import tech.infinitymz.lib.utils.LineReader;
import tech.infinitymz.models.Command;

public class CLIService {

    private LineReader cml;
    private Command commands[];

    public CLIService() {
        System.out.println("Seja Bem-vindo ao Sistema de Gestão Acadêmico!");
        // initCommands();
        initComponents();
    }

    private void initComponents() {
        cml = new LineReader();
        CommandLine();

    }

    private void CommandLine() {
        LinkedList<String> ls = new LinkedList<>();

        String[] b = new String[] { "sdfsd", "sfsdf" };
        String a = "";
        while (true) {
            System.out.print("Creator@bash: ~ $ ");
            a = cml.readString();
            ls.addLast(a);
            if (a.equals("semantica"))
                ls.add(2, a);
        }
    }

    private void initCommands() {
        File file = FileBuffer.createDirectoryPath(FileBuffer.RESOURCE_PATH);
        String content = FileBuffer.readFile(file, FileBuffer.COMMANDS_FILE);
        commands = (Command[]) new Gson().fromJson(content, Command[].class);

    }

}
