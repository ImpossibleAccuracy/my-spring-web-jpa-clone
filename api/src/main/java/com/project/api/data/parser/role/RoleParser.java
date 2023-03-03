package com.project.api.data.parser.role;

import com.project.api.data.model.Role;
import com.project.server.database.repository.parser.ModelParser;

import java.sql.ResultSet;

public class RoleParser implements ModelParser<Role> {
    @Override
    public Role parse(ResultSet resultSet) throws Throwable {
        Role role = new Role();

        role.setId(resultSet.getInt("Id"));
        role.setTitle(resultSet.getString("Title"));

        return role;
    }
}
