package org.project.server.handler;

import com.sun.net.httpserver.HttpExchange;
import org.project.server.HttpController;
import org.project.server.data.HttpMethod;
import org.project.server.exception.http.MethodNotAllowedException;
import org.project.server.utils.AnnotationUtils;
import org.project.server.utils.UrlUtils;

import java.lang.reflect.Method;
import java.net.MalformedURLException;

public abstract class RouterHandler<T extends HttpController> extends RequestHandler<T> {
    protected RouterHandler(String basePath, T controller) {
        super(basePath, controller);

        if (!UrlUtils.isUrlPathValid(basePath)) {
            throw new IllegalArgumentException("Base path is invalid url string");
        }
    }

    /**
     * Handling route
     *
     * @param route    Current route url
     * @param method   Current route method
     * @param exchange Connection info
     * @return Data to write in response
     * @throws Throwable If an error occurred
     */
    public abstract Object processRoute(String route, Method method, HttpExchange exchange)
            throws Throwable;

    @Override
    public Object processRequest(HttpExchange exchange)
            throws Throwable {
        String currentUrl = exchange.getRequestURI().getPath();
        HttpMethod httpMethod = HttpMethod.valueOf(exchange.getRequestMethod());

        // TODO: move url check to server
        if (!UrlUtils.isUrlPathValid(currentUrl)) {
            return handleException(new MalformedURLException());
        }

        Class<?> controllerClass = getController().getClass();

        for (Method method : controllerClass.getDeclaredMethods()) {
            String route = AnnotationUtils.getRoute(method);

            if (route == null) continue;
            else route = buildUrl(getBasePath(), route);

            if (!UrlUtils.isUrlSameIgnorePathParams(route, currentUrl)) continue;

            if (!AnnotationUtils.getHttpMethods(method).contains(httpMethod)) {
                throw new MethodNotAllowedException();
            }

            return processRoute(route, method, exchange);
        }

        // TODO: replace 404 to no result mark
        return "404 Not Found!";
    }

    private String buildUrl(String baseUrl, String routeUrl) {
        if (baseUrl.endsWith("/")) baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        return baseUrl + routeUrl;
    }
}
