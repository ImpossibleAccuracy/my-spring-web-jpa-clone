package com.project.server.database.repository.proxy;

import com.project.server.database.repository.invoker.QueryInvoker;

import java.lang.reflect.Method;

public class RepositoryProxy extends BaseProxy {
    private final QueryInvoker executor = new QueryInvoker();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = super.invoke(proxy, method, args);
        if (result != null) return result;

        return executor.invokeQuery(method, args);
    }
}
