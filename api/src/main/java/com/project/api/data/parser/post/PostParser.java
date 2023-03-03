package com.project.api.data.parser.post;

import com.project.api.data.model.Post;
import com.project.api.data.model.User;
import com.project.api.data.repository.UserRepository;
import com.project.server.database.repository.RepositoryStore;
import com.project.server.database.repository.parser.ModelParser;

import java.sql.ResultSet;

public class PostParser implements ModelParser<Post> {
    private final UserRepository userRepository;

    public PostParser() {
        userRepository = RepositoryStore.getRepository(UserRepository.class);
    }

    @Override
    public Post parse(ResultSet resultSet) throws Throwable {
        int authorId = resultSet.getInt("AuthorId");

        User author = userRepository
                .findById(authorId)
                .orElse(null);

        return Post.builder()
                .id(resultSet.getInt("Id"))
                .title(resultSet.getString("Title"))
                .content(resultSet.getString("Body"))
                .created(resultSet.getTimestamp("CreatedOn").toInstant())
                .author(author)
                .build();
    }
}
