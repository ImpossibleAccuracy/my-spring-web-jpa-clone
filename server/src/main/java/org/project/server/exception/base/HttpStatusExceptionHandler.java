package org.project.server.exception.base;

import org.project.server.exception.handler.ExceptionHandler;
import org.project.server.exception.http.HttpStatusException;

public class HttpStatusExceptionHandler implements ExceptionHandler<HttpStatusException> {
    @Override
    public Object handle(HttpStatusException exception) {
        return ErrorResponse.builder()
                .status(exception.getStatus())
                .error(exception.getMessage())
                .build();
    }
}
