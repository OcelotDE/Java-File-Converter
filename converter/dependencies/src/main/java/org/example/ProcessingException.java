package org.example;

public class ProcessingException extends Exception {
    public ProcessingException(Exception e) {
        super(e);
    }
}
