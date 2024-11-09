package tech.infinitymz.exceptions;

public class NumberLimitExceededException extends RuntimeException {
    public NumberLimitExceededException(String m) {
        super(m);
    }

    public NumberLimitExceededException() {
        super("The numeric value exceeded, it must be  between of 0.5 and 30.");
    }
}
