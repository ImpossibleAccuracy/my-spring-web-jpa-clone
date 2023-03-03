package com.project.api.data.repository;

import com.project.api.data.model.User;
import com.project.api.data.parser.user.UserCountParser;
import com.project.api.data.parser.user.UserParser;
import com.project.server.database.annotation.Query;
import com.project.server.database.annotation.ResultParser;
import com.project.server.database.repository.Repository;

import java.util.List;
import java.util.Optional;

@ResultParser(UserParser.class)
public interface UserRepository extends Repository<User> {
    @Query("""
            select u.* from "User" u WHERE u."Id" = ?
            """)
    Optional<User> findById(int id);

    @Query("""
            select u.*
            from "User" u
            """)
    List<User> findAll();

    @ResultParser(UserCountParser.class)
    @Query("""
            select count(u) from "User" u
            """)
    Integer getCount();
}
