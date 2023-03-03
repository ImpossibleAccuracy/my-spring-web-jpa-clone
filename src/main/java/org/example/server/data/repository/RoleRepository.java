package org.example.server.data.repository;

import org.example.server.data.model.Role;
import org.example.server.data.parser.role.RoleParser;
import org.example.server.database.annotation.Query;
import org.example.server.database.annotation.ResultParser;
import org.example.server.database.repository.Repository;

import java.util.List;

@ResultParser(RoleParser.class)
public interface RoleRepository extends Repository<Role> {
    @Query("""
            select r.*
            from "Role" r
            inner join "User_Role" u_r on r."ID" = u_r."RoleID"
            WHERE u_r."UserID" = ?
            """)
    List<Role> findAllByUser_Id(int userId);
}
