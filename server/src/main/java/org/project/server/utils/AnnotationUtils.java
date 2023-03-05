package org.project.server.utils;

import org.project.server.annotation.route.GetMapping;
import org.project.server.annotation.route.PostMapping;
import org.project.server.annotation.route.RequestMapping;
import org.project.server.data.HttpMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AnnotationUtils {
    /**
     * Get url of controller method
     *
     * @param method Controller method
     * @return Returns the URL from the method annotation
     */
    public static String getRoute(Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        if (requestMapping == null) return null;
        else return requestMapping.value();
    }

    /**
     * Get url from {@link RequestMapping} annotation on type
     * @param type Annotated class
     * @return URL if annotated, otherwise null
     */
    public static String getRoute(Class<?> type) {
        RequestMapping requestMapping = type.getAnnotation(RequestMapping.class);

        if (requestMapping == null) return null;
        else return requestMapping.value();
    }

    /**
     * Get methods of controller method
     *
     * @param method Controller method
     * @return Returns the methods from the method annotations
     */
    public static List<HttpMethod> getHttpMethods(Method method) {
        List<HttpMethod> methods = new ArrayList<>();

        if (method.isAnnotationPresent(GetMapping.class)) methods.add(HttpMethod.GET);
        if (method.isAnnotationPresent(PostMapping.class)) methods.add(HttpMethod.POST);

        return methods;
    }
}
