package org.example.server.database.repository.proxy;

import org.example.server.database.Database;
import org.example.server.database.annotation.NoResult;
import org.example.server.database.annotation.Query;
import org.example.server.database.annotation.ResultParser;
import org.example.server.database.exception.AnnotationMissedException;
import org.example.server.database.repository.parser.ModelParser;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RepositoryProxy extends BaseProxy {
    private final Database database;

    public RepositoryProxy() {
        this.database = Database.getInstance();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = super.invoke(proxy, method, args);
        if (result != null) return result;

        return invokeQuery(method, args);
    }


    private Object invokeQuery(Method method, Object[] args) throws Throwable {
        Query query = method.getAnnotation(Query.class);

        ResultSet resultSet = executeQuery(query.value(), args);

        ModelParser<?> parser = getParser(method);

        if (parser == null) return null;

        return parseData(method, parser, resultSet);
    }

    private ResultSet executeQuery(String query, Object[] args) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);

        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                statement.setObject(i + 1, arg);
            }
        }

        return statement.executeQuery();
    }

    private ModelParser<?> getParser(Method method) throws Throwable {
        Class<?> declaringClass = method.getDeclaringClass();

        if (method.isAnnotationPresent(NoResult.class) ||
                declaringClass.isAnnotationPresent(NoResult.class)) {
            return null;
        }

        ResultParser resultParser = Optional
                .ofNullable(method.getAnnotation(ResultParser.class))
                .orElse(declaringClass.getAnnotation(ResultParser.class));

        if (resultParser == null) throw new AnnotationMissedException(ResultParser.class);

        Class<? extends ModelParser<?>> parserClass = resultParser.value();

        return parserClass.getDeclaredConstructor().newInstance();
    }

    private <T> Object parseData(Method method, ModelParser<T> parser, ResultSet resultSet) throws Throwable {
        Class<?> returnType = method.getReturnType();

        if (Collection.class.isAssignableFrom(returnType)) {
            Class<Collection<T>> listResultType = (Class<Collection<T>>) returnType;

            Collection<T> result = getListResultType(listResultType);

            while (resultSet.next()) {
                result.add(parser.parse(resultSet));
            }

            return result;
        } else if (Optional.class.isAssignableFrom(returnType)) {
            if (resultSet.isBeforeFirst()) {
                resultSet.next();
                T parse = parser.parse(resultSet);
                return Optional.of(parse);
            } else {
                return Optional.empty();
            }
        } else {
            if (resultSet.isBeforeFirst()) {
                resultSet.next();
                return parser.parse(resultSet);
            } else {
                return null;
            }
        }
    }

    private <T> Collection<T> getListResultType(Class<Collection<T>> type) {
        if (Set.class.isAssignableFrom(type)) return new HashSet<>();
        else if (Queue.class.isAssignableFrom(type)) return new ArrayDeque<>();
        else return new ArrayList<>();
    }
}
