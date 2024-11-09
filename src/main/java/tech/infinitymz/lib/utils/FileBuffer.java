package tech.infinitymz.lib.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileBuffer {
    public final static String RESOURCE_PATH = ".\\resource\\";
    public final static String COMMANDS_FILE = "commands.json";

    /**
     * Creates a file, nothing else to say LOL
     * 
     * @param fileName    The name of the file
     * @param fileContent The content that have to be put IN THE FILE
     * @param filePath    The path to create the file
     * @see #createDirectoryPath
     */
    public static void createFile(String fileName, String fileContent, File filePath) {

        // Creating the file
        try (FileWriter file = new FileWriter(filePath + File.separator + fileName);) {
            file.write(fileContent);
        } catch (IOException e) {
        }
    }

    /**
     * Creates an object containing the path that you want to access
     * 
     * @param path The Path to access
     * @return filePath object
     */
    public static File createDirectoryPath(String path) {
        final File filePath = new File(path);
        if (!filePath.exists())
            filePath.mkdirs();
        return filePath;
    }

    /**
     * Returns a FileReader object
     * 
     * @param filePath Path of the file
     * @param fileName Name of the file
     * @return FileReader object
     */
    private static FileReader getFileReader(File filePath, String fileName) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(filePath + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fileReader;
    }

    /**
     * Returns a object that reads a file, line by line (BufferedReader)
     * 
     * @param fileReader fileReader Object
     * @return buffered reader object
     */
    private static BufferedReader getBufferedReader(FileReader fileReader) {
        return new BufferedReader(fileReader);
    }

    /**
     * Receives the line read by the "BufferedReader" and transforms it into a
     * string
     *
     * @return the line in String type
     * @see #loadAndReadFile
     */
    public static String readFile(File userPath, String fileName) {
        final String fileSeparator = "\\";
        String file = fileSeparator + fileName;

        final FileReader fileReader = getFileReader(userPath, file);
        final BufferedReader linReader = getBufferedReader(fileReader);

        String userData = "";

        try {
            String line = "";
            while ((line = linReader.readLine()) != null) {
                userData += line;
            }
        } catch (IOException e) {
            System.out.println("Erro na leitura de dados!");
        }

        return userData;
    }

}
