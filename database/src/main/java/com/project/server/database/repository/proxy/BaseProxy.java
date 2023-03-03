package com.project.server.database.repository.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public abstract class BaseProxy implements InvocationHandler {
    public static final Method HASH_CODE;
    public static final Method EQUALS;
    public static final Method TO_STRING;

    static {
        Class<Object> object = Object.class;
        try {
            HASH_CODE = object.getDeclaredMethod("hashCode");
            EQUALS = object.getDeclaredMethod("equals", object);
            TO_STRING = object.getDeclaredMethod("toString");
        } catch (NoSuchMethodException e) {
            throw new Error(e);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.equals(HASH_CODE)) {
            return objectHashCode(proxy);
        }
        else if (method.equals(EQUALS)) {
            return objectEquals(proxy, args[0]);
        }
        else if (method.equals(TO_STRING)) {
            return objectToString(proxy);
        }

        return null;
    }

    public String objectClassName(Object obj) {
        return obj.getClass().getName();
    }

    public int objectHashCode(Object obj) {
        return System.identityHashCode(obj);
    }

    public boolean objectEquals(Object obj, Object other) {
        return obj == other;
    }

    public String objectToString(Object obj) {
        return objectClassName(obj) + '@' + Integer.toHexString(objectHashCode(obj));
    }
}
