package org.example.common;

public class ApplicationException extends RuntimeException {
    public ApplicationException(Exception e) {
        super(e);
    }
}
