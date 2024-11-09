package tech.infinitymz.exceptions;

public class CharacterLimitExceededException extends RuntimeException {
    public CharacterLimitExceededException(String m) {
        super(m);
    }

    public CharacterLimitExceededException() {
        super("Number of characters has exceeded, The allowed range is in between 1 to 60.");
    }
}
