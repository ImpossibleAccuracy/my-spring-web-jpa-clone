package org.project.server.exception.http;

public class BadRequestException extends HttpStatusException {
    public BadRequestException(String message) {
        super(401, message);
    }
}
