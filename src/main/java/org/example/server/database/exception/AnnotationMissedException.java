package org.example.server.database.exception;

public class AnnotationMissedException extends RuntimeException {
    public AnnotationMissedException(Class<?> aClass) {
        super("Missed annotation \"@%s\"".formatted(aClass.getSimpleName()));
    }
}
