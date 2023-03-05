package org.project.server.exception.handler;

import java.util.HashMap;
import java.util.Map;

public class ExceptionHandlerStore {
    private final Map<Class<? extends Throwable>, ExceptionHandler<?>> handlers;

    private ExceptionHandler<Throwable> defaultHandler;

    public ExceptionHandlerStore() {
        this.handlers = new HashMap<>();
    }

    /**
     * Attaches default handler used when no suitable handler found
     * @param defaultHandler Default handler
     */
    public void setDefaultHandler(ExceptionHandler<Throwable> defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    public <T extends Throwable> void attachHandler(Class<T> exceptionType, ExceptionHandler<T> handler) {
        handlers.put(exceptionType, handler);
    }

    public void clearHandlers() {
        handlers.clear();
    }

    public <T extends Throwable> Object handleException(T throwable) {
        ExceptionHandler<T> handler = findHandler(throwable.getClass());

        if (handler == null && defaultHandler == null) return throwable;
        else if (handler != null) return handler.handle(throwable);
        else return defaultHandler.handle(throwable);
    }

    /**
     * Find assignable handler in handlers map
     */
    private <T extends Throwable> ExceptionHandler<T> findHandler(Class<?> tClass) {
        for (Class<? extends Throwable> key : handlers.keySet()) {
            if (key.isAssignableFrom(tClass)) {
                return (ExceptionHandler<T>) handlers.get(key);
            }
        }

        return null;
    }
}
