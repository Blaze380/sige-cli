package tech.infinitymz;

import tech.infinitymz.lib.utils.Terminal;
import tech.infinitymz.services.CLIService;

public final class App {
    public static void main(String[] args) {
        Terminal.create();
        new CLIService();
        // System.out.println(0x7FFFFFFF);
    }
}
