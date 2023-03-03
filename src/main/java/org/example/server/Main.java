package org.example.server;

import org.example.server.data.repository.PostRepository;
import org.example.server.data.repository.UserRepository;
import org.example.server.database.ConnectionInfo;
import org.example.server.database.Database;
import org.example.server.database.repository.RepositoryStore;

public class Main {
    public static void main(String[] args) {
        ConnectionInfo info = new ConnectionInfo("localhost",
                5432,
                "SimpleDatabase",
                "root",
                "1234");

        Database.initialize(info);

        UserRepository userRepository = RepositoryStore.getRepository(UserRepository.class);
        System.out.printf("User table contains %d rows%n", userRepository.getCount());

        PostRepository postRepository = RepositoryStore.getRepository(PostRepository.class);
        postRepository.findAllByAuthor_Id(1).forEach(System.out::println);
    }
}