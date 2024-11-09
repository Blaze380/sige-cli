package tech.infinitymz;

import tech.infinitymz.services.CLIService;

/**
 * Hello world!
 */
public final class App {
    /**
     * Says hello to the world.
     * 
     * @param args The arguments of the program.
     * @throws InterruptedException
     */
    public static void main(String[] args) {
        System.out.println(0x7FFFFFFF);
        new CLIService();
    }

    static void npmLoading() throws InterruptedException {
        // System.out.println("\u2764 \tMiga \taaaaaaaaaaaa \tkjskdjskj");
        System.out.printf("%-10s %-5s", "Nome", "idadeaaaaaaaaaaaaaaaaaaa\n");
        System.out.printf("%-10s %-5s", "idadeaaaaaaaaaaaaaaaaaaa", "Nome");
    }

    static void simpleLoading() throws InterruptedException {
        for (int i = 0; i <= 10; i++) {
            System.out.print(
                    "\r\033[0m Carregando: [" + ("\033[1;41m " + ".".repeat(i) + "\033[0m") + " ".repeat(10 - i)
                            + " ] \033[0m"
                            + (i * 10) + "%");
            Thread.sleep(500);
        }

    }
}
