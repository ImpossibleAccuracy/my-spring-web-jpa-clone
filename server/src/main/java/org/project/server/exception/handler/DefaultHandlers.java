package org.project.server.exception.handler;

import org.project.server.exception.base.HttpStatusExceptionHandler;
import org.project.server.exception.http.HttpStatusException;

public class DefaultHandlers {
    public static void attachBaseHandlers(ExceptionHandlerStore store) {
        store.attachHandler(HttpStatusException.class, new HttpStatusExceptionHandler());
    }
}
