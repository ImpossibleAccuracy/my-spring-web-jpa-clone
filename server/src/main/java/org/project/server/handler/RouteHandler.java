package org.project.server.handler;

import com.sun.net.httpserver.HttpExchange;
import org.jetbrains.annotations.NotNull;
import org.project.server.HttpController;
import org.project.server.annotation.GetParam;
import org.project.server.annotation.PathVariable;
import org.project.server.annotation.RequestBody;
import org.project.server.data.RequestData;
import org.project.server.exception.http.BadRequestException;
import org.project.server.utils.AnnotationUtils;
import org.project.server.utils.RequestParser;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URI;

public class RouteHandler<T extends HttpController> extends RouterHandler<T> {
    private final RequestParser parser = new RequestParser();

    public RouteHandler(String basePath, T controller) {
        super(basePath, controller);
    }

    @Override
    public Object processRoute(String route, Method method, HttpExchange exchange)
            throws Throwable {
        RequestData requestData = getRequestData(method, exchange, route);

        Object controller = getController();
        Object[] args = collectArguments(method, requestData);

        AnnotationUtils.getRoute(getClass());

        try {
            return method.invoke(controller, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    /**
     * Collect method arguments from request data
     *
     * @param method      Controller method
     * @param requestData Request info
     * @return Ordered method arguments
     */
    private Object[] collectArguments(Method method,
                                      RequestData requestData) {
        Object[] args = new Object[method.getParameterCount()];
        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Object value = null;

            if (parameter.isAnnotationPresent(PathVariable.class)) {
                PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
                String name = pathVariable.value();

                value = requestData.getPathParams().get(name);
            } else if (parameter.isAnnotationPresent(GetParam.class)) {
                GetParam getParam = parameter.getAnnotation(GetParam.class);
                String name = getParam.value();

                value = requestData.getQueryParams().get(name);
            } else if (parameter.isAnnotationPresent(RequestBody.class)) {
                value = requestData.getBody();
            }

            if (value == null) {
                args[i] = null;
            } else {
                args[i] = getValueIfTypeAssignable(parameter, value);
            }
        }

        return args;
    }

    /**
     * Checks type of parameter and type of value and returns value if they match.
     *
     * @param parameter Controller method parameter
     * @param value     Value to pass into args
     * @return Returns value, if types are the same
     * @throws BadRequestException If types are not the same
     */
    private Object getValueIfTypeAssignable(@NotNull Parameter parameter, @NotNull Object value)
            throws BadRequestException {
        Class<?> parameterType = parameter.getType();
        Class<?> valueClass = value.getClass();

        if (!parameter.getType().isAssignableFrom(valueClass)) {
            throw new BadRequestException(
                    "Cannot cast %s to %s".formatted(
                            valueClass.getSimpleName(),
                            parameterType.getSimpleName()));
        }

        return value;
    }

    /**
     * Collect request data to single object
     *
     * @param method   Controller method
     * @param exchange Connection info
     * @param route    Current route
     * @return Request data
     * @throws IOException If an error occurred while reading the data
     */
    private RequestData getRequestData(Method method, HttpExchange exchange, @NotNull String route) throws IOException {
        URI uri = exchange.getRequestURI();

        RequestData requestData = new RequestData();

        Class<?> bodyType = getBodyType(method);
        if (bodyType != null)
            requestData.setBody(
                    parser.parseBody(exchange.getRequestBody(), bodyType));

        if (hasAnnotatedArgument(method, GetParam.class))
            requestData.setQueryParams(
                    parser.parseGetParams(uri.getQuery()));

        if (hasAnnotatedArgument(method, PathVariable.class))
            requestData.setPathParams(
                    parser.parsePathParams(route, uri.getPath().substring(getBasePath().length())));

        return requestData;
    }

    /**
     * Check method has annotation
     *
     * @param method          Class method
     * @param annotationClass Annotation class
     */
    public boolean hasAnnotatedArgument(Method method, Class<? extends java.lang.annotation.Annotation> annotationClass) {
        for (Parameter parameter : method.getParameters()) {
            if (parameter.isAnnotationPresent(annotationClass)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Finds the method parameter type with {@link RequestBody} annotation.
     *
     * @param method Class method
     * @return Type of the annotated parameter, if present or null
     */
    public Class<?> getBodyType(Method method) {
        for (Parameter parameter : method.getParameters()) {
            if (parameter.isAnnotationPresent(RequestBody.class)) {
                return parameter.getType();
            }
        }

        return null;
    }
}
