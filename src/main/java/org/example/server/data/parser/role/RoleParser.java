package org.example.server.data.parser.role;

import org.example.server.data.model.Role;
import org.example.server.database.repository.parser.ModelParser;

import java.sql.ResultSet;

public class RoleParser implements ModelParser<Role> {
    @Override
    public Role parse(ResultSet resultSet) throws Throwable {
        Role role = new Role();

        role.setId(resultSet.getInt("ID"));
        role.setTitle(resultSet.getString("ID"));

        return role;
    }
}
