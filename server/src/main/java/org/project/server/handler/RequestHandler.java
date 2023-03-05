package org.project.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.project.server.HttpController;
import org.project.server.exception.handler.ExceptionHandlerStore;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

// TODO: think of a good name

/**
 * Base handler class
 *
 * @param <T> Controller type
 */
public abstract class RequestHandler<T extends HttpController> implements HttpHandler {
    private static final ObjectMapper mapper = new ObjectMapper();

    private final String basePath;
    private final T controller;

    private ExceptionHandlerStore exceptionHandlerStore;

    protected RequestHandler(String basePath, T controller) {
        this.basePath = basePath;
        this.controller = controller;
    }

    public String getBasePath() {
        return basePath;
    }

    public T getController() {
        return controller;
    }

    public void setExceptionHandlerStore(ExceptionHandlerStore exceptionHandlerStore) {
        this.exceptionHandlerStore = exceptionHandlerStore;
    }

    /**
     * Handling new connection
     *
     * @param exchange Connection info
     * @return Data to write in response
     * @throws Throwable If an error occurred
     */
    public abstract Object processRequest(HttpExchange exchange)
            throws Throwable;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Object result;
        int status;

        try {
            result = processRequest(exchange);
            status = 200;
        } catch (Throwable t) {
            result = handleException(t);
            status = 500;
        }

        sendResponse(exchange, status, result);
    }

    /**
     * Handles the thrown exception and returns the result. Uses {@link #exceptionHandlerStore} if present.
     *
     * @param throwable Thrown exception
     * @return Data to write in response
     */
    public Object handleException(Throwable throwable) {
        if (this.exceptionHandlerStore == null) return null;
        return exceptionHandlerStore.handleException(throwable);
    }

    /**
     * Writes controller result to output stream and close connection
     *
     * @param exchange Current connection
     * @param status   Response status
     * @param result   Response body
     * @throws IOException If result is not serializable or can not be sent
     */
    protected void sendResponse(HttpExchange exchange, int status, Object result)
            throws IOException {
        byte[] response = convertResultToBytes(result);

        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, response.length);

        OutputStream out = exchange.getResponseBody();
        out.write(response);
        out.flush();

        out.close();
        exchange.close();
    }

    /**
     * Convert object to bytes
     *
     * @param result Object to convert
     * @return Converted data
     * @throws IOException If result can not be serialized
     */
    private byte[] convertResultToBytes(Object result) throws IOException {
        if (result == null) return new byte[0];
        else if (result instanceof byte[]) {
            return (byte[]) result;
        } else if (result instanceof String) {
            return ((String) result).getBytes(StandardCharsets.UTF_8);
        } else {
            return mapper
                    .writeValueAsString(result)
                    .getBytes(StandardCharsets.UTF_8);
        }
    }
}
