package com.project.server.database.repository;

import com.project.server.database.repository.proxy.RepositoryProxy;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class RepositoryStore {
    private static final Map<Class<?>, Repository<?>> repositoryMap = new HashMap<>();

    public static <M, R extends Repository<M>>
    R getRepository(Class<R> tClass) {
        if (!tClass.isInterface()) throw new IllegalArgumentException("The provided class is not an interface");

        if (repositoryMap.containsKey(tClass)) {
            return (R) repositoryMap.get(tClass);
        }

        R instance = (R) Proxy.newProxyInstance(
                tClass.getClassLoader(),
                new Class[]{tClass},
                new RepositoryProxy());

        repositoryMap.put(tClass, instance);

        return instance;
    }
}
