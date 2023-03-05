package org.project.server.exception.handler;

public interface ExceptionHandler<T extends Throwable> {
    Object handle(T exception);
}
