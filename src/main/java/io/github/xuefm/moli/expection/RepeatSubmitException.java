package io.github.xuefm.moli.expection;

public class RepeatSubmitException extends RuntimeException {
    public RepeatSubmitException(String message) {
        super(message);
    }
}
