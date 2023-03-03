package com.project.api.data.parser.user;

import com.project.api.data.model.User;
import com.project.api.data.repository.RoleRepository;
import com.project.server.database.repository.RepositoryStore;
import com.project.server.database.repository.parser.ModelParser;

import java.sql.ResultSet;

public class UserParser implements ModelParser<User> {
    private final RoleRepository roleRepository;

    public UserParser() {
        roleRepository = RepositoryStore.getRepository(RoleRepository.class);
    }

    @Override
    public User parse(ResultSet resultSet) throws Throwable {
        User user = new User();

        user.setId(resultSet.getInt("Id"));
        user.setUsername(resultSet.getString("Username"));
        user.setPassword(resultSet.getString("Password"));
        user.setCreated(resultSet.getTimestamp("CreatedOn").toInstant());

        user.setRoles(roleRepository.findAllByUser_Id(user.getId()));

        return user;
    }
}
