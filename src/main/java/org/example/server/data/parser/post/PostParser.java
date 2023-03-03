package org.example.server.data.parser.post;

import org.example.server.data.model.Post;
import org.example.server.data.model.User;
import org.example.server.data.repository.UserRepository;
import org.example.server.database.repository.RepositoryStore;
import org.example.server.database.repository.parser.ModelParser;

import java.sql.ResultSet;

public class PostParser implements ModelParser<Post> {
    private final UserRepository userRepository;

    public PostParser() {
        userRepository = RepositoryStore.getRepository(UserRepository.class);
    }

    @Override
    public Post parse(ResultSet resultSet) throws Throwable {
        int authorId = resultSet.getInt("AuthorID");

        User author = userRepository
                .findById(authorId)
                .orElse(null);

        return Post.builder()
                .id(resultSet.getInt("ID"))
                .title(resultSet.getString("Title"))
                .content(resultSet.getString("Content"))
                .created(resultSet.getTimestamp("CreatedOn").toInstant())
                .author(author)
                .build();
    }
}
