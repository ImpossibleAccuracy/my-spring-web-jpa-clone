package org.project.server.handler;

import com.sun.net.httpserver.HttpExchange;
import org.project.server.annotation.route.GetMapping;
import org.project.server.annotation.route.PostMapping;
import org.project.server.utils.UrlUtils;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public abstract class UrlCheckHttpHandler<T> extends WritableHttpHandler<T> {
    protected UrlCheckHttpHandler(String basePath, T controller) {
        super(basePath, controller);
    }

    public abstract void process(String route, HttpExchange exchange, Method method) throws Throwable;

    @Override
    public void handle(HttpExchange exchange) {
        try {
            if (!UrlUtils.isUrlValid(exchange.getRequestURI().getRawPath())) {
                sendResponseAndClose(exchange, 400, "Invalid url".getBytes(StandardCharsets.UTF_8));
                return;
            }

            for (Method method : controllerClass.getDeclaredMethods()) {
                String route = getRouteUrl(exchange, method);

                if (route != null &&
                        checkRouteMatch(route, exchange.getRequestURI().getPath())) {
                    process(route, exchange, method);
                    break;
                }
            }
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }

    private String getRouteUrl(HttpExchange exchange, Method method) {
        String url = null;

        switch (exchange.getRequestMethod()) {
            case "GET" -> {
                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                if (getMapping == null) return null;
                url = getMapping.value();
            }
            case "POST" -> {
                PostMapping postMapping = method.getAnnotation(PostMapping.class);
                if (postMapping == null) return null;
                url = postMapping.value();
            }
        }

        if (url == null) {
            System.err.printf("Unknown request method: %s%n", exchange.getRequestMethod());
            return null;
        }

        return url;
    }

    private boolean checkRouteMatch(String url, String requestPath) {
        return checkRouteByParts(basePath + url, requestPath);
    }

    private boolean checkRouteByParts(String route, String actualUrl) {
        return UrlUtils.isUrlSameIgnorePathParams(route, actualUrl);
    }
}
