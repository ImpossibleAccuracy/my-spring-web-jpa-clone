package com.example.server.database.test.repository;

import com.example.server.database.test.parser.user.UserCountParser;
import com.example.server.database.test.parser.user.UserParser;
import org.example.server.data.model.User;
import org.example.server.database.annotation.Query;
import org.example.server.database.annotation.ResultParser;
import org.example.server.database.repository.Repository;

import java.util.List;
import java.util.Optional;

@ResultParser(UserParser.class)
public interface UserRepository extends Repository<User> {
    @Query("select u.* from \"User\" u")
    List<User> findAll();

    @Query("select u.* from \"User\" u WHERE u.\"ID\" = ?")
    Optional<User> findById(int id);

    @ResultParser(UserCountParser.class)
    @Query("select count(u) from \"User\" u")
    Integer getCount();
}
