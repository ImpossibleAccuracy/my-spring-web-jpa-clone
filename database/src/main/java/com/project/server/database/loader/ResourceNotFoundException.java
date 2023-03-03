package com.project.server.database.loader;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super("Resource \"%s\" does not exist".formatted(message));
    }
}
