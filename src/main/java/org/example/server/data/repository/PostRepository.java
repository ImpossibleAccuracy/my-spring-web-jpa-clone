package org.example.server.data.repository;

import org.example.server.data.model.Post;
import org.example.server.data.parser.post.PostParser;
import org.example.server.database.annotation.Query;
import org.example.server.database.annotation.ResultParser;
import org.example.server.database.repository.Repository;

import java.util.Set;

@ResultParser(PostParser.class)
public interface PostRepository extends Repository<Post> {
    @Query("""
            select p.*
            from "Post" p
            WHERE p."AuthorID" = ?
            """)
    Set<Post> findAllByAuthor_Id(int authorId);
}
