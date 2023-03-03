package org.project.server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public abstract class WritableHttpHandler<T> implements HttpHandler {
    protected final String basePath;

    protected final T controller;
    protected final Class<T> controllerClass;

    protected WritableHttpHandler(String basePath, T controller) {
        this.basePath = basePath;
        this.controller = controller;
        this.controllerClass = (Class<T>) controller.getClass();
    }

    public void sendResponseAndClose(HttpExchange exchange, int status, byte[] response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, response.length);

        OutputStream out = exchange.getResponseBody();
        out.write(response);
        out.flush();

        out.close();
        exchange.close();
    }
}
