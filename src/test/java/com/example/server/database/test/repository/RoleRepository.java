package com.example.server.database.test.repository;

import com.example.server.database.test.parser.role.RoleParser;
import org.example.server.data.model.Role;
import org.example.server.database.annotation.Query;
import org.example.server.database.annotation.ResultParser;
import org.example.server.database.repository.Repository;

import java.util.List;
import java.util.Optional;

@ResultParser(RoleParser.class)
public interface RoleRepository extends Repository<Role> {
    @Query("""
            select r.* from "Role" r WHERE r."ID" = ?
            """)
    Optional<Role> findById(int id);

    @Query("""
            select r.* from "Role" r
            """)
    List<Role> findAll();

    @Query("""
            select r.*
            from "Role" r
            inner join "User_Role" u_r on r."ID" = u_r."RoleID"
            WHERE u_r."UserID" = ?
            """)
    List<Role> findAllByUser_Id(int userId);
}
