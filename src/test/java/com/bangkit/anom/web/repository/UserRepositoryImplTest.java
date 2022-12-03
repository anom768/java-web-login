package com.bangkit.anom.web.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.bangkit.anom.web.entity.User;
import com.bangkit.anom.web.util.DatabaseUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserRepositoryImplTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryImpl(DatabaseUtil.getDataSource());

        userRepository.deleteAll();
    }

    @Test
    void testInsert() {
        User user = new User("anom", "Anom", "anom");
        var result = userRepository.insert(user);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getPassword(), result.getPassword());
    }

    @Test
    void testUpdate() {
        var user = userRepository.insert(new User("anom", "Anom", "anom"));
        var result = userRepository.update(new User(user.getId(), "Bangkit", "bangkit"));

        assertNotNull(user);
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals("Bangkit", result.getName());
        assertEquals("bangkit", result.getPassword());
        assertNotEquals(user.getName(), result.getName());
        assertNotEquals(user.getPassword(), result.getPassword());
    }

    @Test
    void testFindByIdNotFound() {
        var result = userRepository.findById("anom");
        assertNull(result);
    }

    @Test
    void testFindByIdSuccess() {
        var user = userRepository.insert(new User("anom", "Anom", "anom"));
        var result = userRepository.findById("anom");

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
    }

}

