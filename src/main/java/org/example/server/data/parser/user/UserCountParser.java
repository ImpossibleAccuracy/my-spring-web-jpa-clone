package org.example.server.data.parser.user;

import org.example.server.database.repository.parser.ModelParser;

import java.sql.ResultSet;

public class UserCountParser implements ModelParser<Integer> {
    @Override
    public Integer parse(ResultSet resultSet) throws Throwable {
        return resultSet.getInt(1);
    }
}
