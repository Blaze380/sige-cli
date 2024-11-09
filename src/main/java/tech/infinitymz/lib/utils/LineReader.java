package tech.infinitymz.lib.utils;

import java.util.Scanner;

public class LineReader {

    final private Scanner scanner;

    public LineReader() {
        scanner = new Scanner(System.in);
    }

    public String readString() {
        final String str = scanner.nextLine();
        return str;
    }

    public double readDouble() {
        final double dbl = scanner.nextDouble();
        return dbl;
    }

    public double readInteger() {
        final int inT = scanner.nextInt();
        return inT;
    }

    public void clearConsole() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
