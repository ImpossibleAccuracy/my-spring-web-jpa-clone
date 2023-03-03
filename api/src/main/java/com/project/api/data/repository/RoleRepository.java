package com.project.api.data.repository;

import com.project.api.data.model.Role;
import com.project.api.data.parser.role.RoleParser;
import com.project.server.database.annotation.Query;
import com.project.server.database.annotation.ResultParser;
import com.project.server.database.repository.Repository;

import java.util.List;

@ResultParser(RoleParser.class)
public interface RoleRepository extends Repository<Role> {
    @Query("""
            select r.*
            from "Role" r
            inner join "User_Role" u_r on r."Id" = u_r."RoleId"
            WHERE u_r."UserId" = ?
            """)
    List<Role> findAllByUser_Id(int userId);
}
