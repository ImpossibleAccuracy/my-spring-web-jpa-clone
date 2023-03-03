package com.project.api;

import com.project.api.data.model.Post;
import com.project.api.data.model.User;
import com.project.api.data.repository.PostRepository;
import com.project.api.data.repository.UserRepository;
import com.project.server.database.ConnectionInfo;
import com.project.server.database.Database;
import com.project.server.database.loader.DatabasePropertyLoader;
import com.project.server.database.repository.RepositoryStore;

import java.io.IOException;
import java.util.List;

public class SimpleApiServerApplication {
    public static void main(String[] args) throws IOException {
        ConnectionInfo info = DatabasePropertyLoader.loadFromResource("database.yml");

        Database.initialize(info);

        UserRepository userRepository = RepositoryStore.getRepository(UserRepository.class);
        PostRepository postRepository = RepositoryStore.getRepository(PostRepository.class);

        System.out.printf("User table contains %d rows%n", userRepository.getCount());

        long userD1 = System.currentTimeMillis();
        List<User> users = userRepository.findAll();
        long userD2 = System.currentTimeMillis();

        System.out.printf("%d users loaded by %d ms%n", users.size(), userD2 - userD1);

        long postsD1 = System.currentTimeMillis();
        users.forEach(user -> {
            long postD1 = System.currentTimeMillis();

            List<Post> posts = postRepository.findAllByAuthor_Id(user.getId());

            long postD2 = System.currentTimeMillis();

            System.out.printf(
                    "User(%d) actually have %d posts. Time spent per request %d ms%n",
                    user.getId(),
                    posts.size(),
                    postD2 - postD1);
        });
        long postsD2 = System.currentTimeMillis();

        System.out.printf("All test time: %d ms%n", postsD2 - postsD1);
    }
}