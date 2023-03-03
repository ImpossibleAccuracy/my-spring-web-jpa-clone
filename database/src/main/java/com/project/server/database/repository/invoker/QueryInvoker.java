package com.project.server.database.repository.invoker;

import com.project.server.database.Database;
import com.project.server.database.annotation.NoResult;
import com.project.server.database.annotation.Query;
import com.project.server.database.annotation.ResultParser;
import com.project.server.database.exception.AnnotationMissedException;
import com.project.server.database.repository.parser.ModelParser;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class QueryInvoker {
    private final Database database;

    public QueryInvoker() {
        this.database = Database.getInstance();
    }

    public Object invokeQuery(Method method, Object[] args) throws Throwable {
        Query query = method.getAnnotation(Query.class);

        if (query == null) throw new AnnotationMissedException(Query.class);

        ResultSet resultSet = executeQuery(query.value(), args);

        if (hasResult(method)) {
            ModelParser<?> parser = getParser(method);

            return parseData(method, parser, resultSet);
        }

        return null;
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

    public boolean hasResult(Method method) {
        return !(method.isAnnotationPresent(NoResult.class) ||
                method.getDeclaringClass().isAnnotationPresent(NoResult.class));
    }

    private ModelParser<?> getParser(Method method) throws Throwable {
        Class<?> declaringClass = method.getDeclaringClass();

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
            Class<Collection<T>> wrapperType = (Class<Collection<T>>) returnType;
            return parseCollection(wrapperType, parser, resultSet);
        } else {
            Object result = parseModel(parser, resultSet);

            if (Optional.class.isAssignableFrom(returnType)) {
                return Optional.ofNullable(result);
            } else {
                return result;
            }
        }
    }

    private <T> T parseModel(ModelParser<T> parser, ResultSet resultSet) throws Throwable {
        if (resultSet.isBeforeFirst()) {
            resultSet.next();
            return parser.parse(resultSet);
        } else {
            return null;
        }
    }

    private <T> Collection<T> parseCollection(Class<Collection<T>> listResultType, ModelParser<T> parser, ResultSet resultSet)
            throws Throwable {
        Collection<T> result = createWrapperForType(listResultType);

        while (resultSet.next()) {
            result.add(parser.parse(resultSet));
        }

        return result;
    }

    private <T> Collection<T> createWrapperForType(Class<Collection<T>> type) {
        if (Set.class.isAssignableFrom(type)) return new HashSet<>();
        else if (Queue.class.isAssignableFrom(type)) return new ArrayDeque<>();
        else return new ArrayList<>();
    }
}
