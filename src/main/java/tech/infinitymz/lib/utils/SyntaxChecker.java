package tech.infinitymz.lib.utils;

public class SyntaxChecker {
    public static boolean isCommandCorrect(String prefix, String cmd, int args) {
        if(!isCommandContainSpaces(cmd)) return false;
        if(!isPrefixCorrect(cmd,prefix))return false;
        return true;
    }
    public static String wrapCommand(String cmd){
        return "";
    }

    private static boolean isCommandContainSpaces(String cmd){
        return true;
    }

    private static boolean isPrefixCorrect(String cmd, String prefix) {
        return true;
    }
}
