package org.project.server.exception.http;

public class HttpStatusException extends RuntimeException {
    private final int status;
    private final String message;

    public HttpStatusException(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
