package com.example.server.database.test;

import com.example.server.database.test.repository.NotRepository;
import com.example.server.database.test.repository.UserRepository;
import org.example.server.database.ConnectionInfo;
import org.example.server.database.Database;
import org.example.server.database.repository.RepositoryStore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RepositoriesTest {
    @BeforeAll
    public static void setup() {
        ConnectionInfo info = new ConnectionInfo("localhost",
                5432,
                "SimpleDatabase",
                "root",
                "1234");

        Database.initialize(info);
    }

    @Test
    public void whenGetRepository_thanArgumentIsRepository() {
        assertDoesNotThrow(
                () -> RepositoryStore.getRepository(UserRepository.class));
    }

    @Test
    public void whenGetRepository_thanArgumentNotRepository() {
        assertThrows(
                IllegalArgumentException.class,
                () -> RepositoryStore.getRepository(NotRepository.class));
    }

    @Test
    public void whenGetRepositoryCorrectly_thenGetRepository() {
        UserRepository userRepository = RepositoryStore.getRepository(UserRepository.class);

        assertInstanceOf(UserRepository.class, userRepository);
    }

    @Test
    public void whenGetRepositoryTwice_thenReturnSingleObject() {
        UserRepository userRepository = RepositoryStore.getRepository(UserRepository.class);
        UserRepository userRepository2 = RepositoryStore.getRepository(UserRepository.class);

        assertEquals(userRepository, userRepository2);

        UserRepository userRepository3 = RepositoryStore.getRepository(UserRepository.class);

        assertEquals(userRepository, userRepository3);
        assertEquals(userRepository2, userRepository3);

        assertNotEquals(userRepository, null);
        assertNotEquals(userRepository, new Object());
    }
}
