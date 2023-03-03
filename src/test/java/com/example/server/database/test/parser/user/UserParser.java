package com.example.server.database.test.parser.user;

import com.example.server.database.test.repository.RoleRepository;
import org.example.server.data.model.User;
import org.example.server.database.repository.RepositoryStore;
import org.example.server.database.repository.parser.ModelParser;

import java.sql.ResultSet;

public class UserParser implements ModelParser<User> {
    private final RoleRepository roleRepository;

    public UserParser() {
        roleRepository = RepositoryStore.getRepository(RoleRepository.class);
    }

    @Override
    public User parse(ResultSet resultSet) throws Throwable {
        User user = new User();

        user.setId(resultSet.getInt("ID"));
        user.setUsername(resultSet.getString("Username"));
        user.setPassword(resultSet.getString("Password"));
        user.setCreated(resultSet.getTimestamp("CreatedOn").toInstant());

        user.setRoles(roleRepository.findAllByUser_Id(user.getId()));

        return user;
    }
}
