package org.example.common;

public class ApplicationException extends RuntimeException {
    public ApplicationException(Exception e) {
        super(e);
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Exception e) {
        super(message, e);
    }
}
