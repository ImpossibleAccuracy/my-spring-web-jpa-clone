package com.example.server.database.test.repository;

import com.example.server.database.test.parser.post.PostParser;
import org.example.server.data.model.Post;
import org.example.server.database.annotation.Query;
import org.example.server.database.annotation.ResultParser;
import org.example.server.database.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@ResultParser(PostParser.class)
public interface PostRepository extends Repository<Post> {
    @Query("""
            select p.* from "Post" p WHERE p."ID" = ?
            """)
    Optional<Post> findById(int id);

    @Query("""
            select p.* from "Post" p
            """)
    List<Post> findAll();

    @Query("""
            select p.*
            from "Post" p
            WHERE p."AuthorID" = ?
            """)
    Set<Post> findAllByAuthor_Id(int authorId);
}
