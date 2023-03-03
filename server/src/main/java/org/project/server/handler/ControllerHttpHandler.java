package org.project.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import org.project.server.RequestParser;
import org.project.server.annotation.GetParam;
import org.project.server.annotation.PathVariable;
import org.project.server.annotation.RequestBody;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ControllerHttpHandler<T> extends UrlCheckHttpHandler<T> {
    private final RequestParser parser = new RequestParser();
    private final ObjectMapper mapper = new ObjectMapper();

    public ControllerHttpHandler(String basePath, T controller) {
        super(basePath, controller);
    }

    @Override
    public void process(String route, HttpExchange exchange, Method method) throws Throwable {
        URI uri = exchange.getRequestURI();

        Object body = null;
        Class<?> bodyType = getBodyType(method);
        if (bodyType != null)
            body = parser.parseBody(exchange.getRequestBody(), bodyType);

        Map<String, String> queryParams = null;
        if (hasAnnotatedArgument(method, GetParam.class))
            queryParams = parser.parseGetParams(uri.getQuery());

        Map<String, String> pathParams = null;
        if (hasAnnotatedArgument(method, PathVariable.class))
            pathParams = parser.parsePathParams(route, uri.getPath().substring(basePath.length()));

        Object result = invokeControllerMethod(method, body, queryParams, pathParams);
        byte[] response = mapper
                .writeValueAsString(result)
                .getBytes(StandardCharsets.UTF_8);

        sendResponseAndClose(exchange, 200, response);
    }

    private Object invokeControllerMethod(Method method,
                                          Object body,
                                          Map<String, String> queryParams,
                                          Map<String, String> pathParams) throws Throwable {
        Object[] args = new Object[method.getParameterCount()];
        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];

            PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
            if (pathVariable != null) {
                String name = pathVariable.value();

                args[i] = pathParams.get(name);
            }

            // TODO: pass other data
        }

        return method.invoke(controller, args);
    }

    public boolean hasAnnotatedArgument(Method method, Class<? extends java.lang.annotation.Annotation> annotationClass) {
        for (Parameter parameter : method.getParameters()) {
            if (parameter.isAnnotationPresent(annotationClass)) {
                return true;
            }
        }

        return false;
    }

    public Class<?> getBodyType(Method method) {
        for (Parameter parameter : method.getParameters()) {
            if (parameter.isAnnotationPresent(RequestBody.class)) {
                return parameter.getType();
            }
        }

        return null;
    }
}
