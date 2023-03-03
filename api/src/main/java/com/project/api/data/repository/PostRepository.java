package com.project.api.data.repository;

import com.project.api.data.model.Post;
import com.project.api.data.parser.post.PostParser;
import com.project.server.database.annotation.Query;
import com.project.server.database.annotation.ResultParser;
import com.project.server.database.repository.Repository;

import java.util.List;

@ResultParser(PostParser.class)
public interface PostRepository extends Repository<Post> {
    @Query("""
            select p.*
            from "Post" p
            """)
    List<Post> findAll();

    @Query("""
            select p.*
            from "Post" p
            WHERE p."AuthorId" = ?
            """)
    List<Post> findAllByAuthor_Id(int authorId);
}
