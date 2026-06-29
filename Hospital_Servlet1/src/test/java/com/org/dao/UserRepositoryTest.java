package com.org.dao;

import com.org.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userRepository = new UserRepository(jdbcTemplate, passwordEncoder);
    }

    @Test
    void registerUser_success() {
        User u = new User("John Doe", "john@example.com", "password123");
        when(passwordEncoder.encode("password123")).thenReturn("$2a$hash");
        when(jdbcTemplate.update(anyString(), anyString(), anyString(), anyString())).thenReturn(1);

        boolean result = userRepository.registerUser(u);

        assertTrue(result);
        verify(jdbcTemplate).update(anyString(), eq("John Doe"), eq("john@example.com"), eq("$2a$hash"));
    }

    @Test
    void registerUser_failure_returnsDbError() {
        User u = new User("John Doe", "john@example.com", "password123");
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$hash");
        when(jdbcTemplate.update(anyString(), anyString(), anyString(), anyString()))
                .thenThrow(new RuntimeException("DB error"));

        boolean result = userRepository.registerUser(u);

        assertFalse(result);
    }

    @Test
    void login_invalidCredentials_returnsNull() {
        when(jdbcTemplate.query(anyString(), any(ResultSetExtractor.class), anyString())).thenReturn(null);

        User result = userRepository.Login("unknown@example.com", "wrongpass");

        assertNull(result);
    }

    @Test
    void changePassword_success() {
        when(passwordEncoder.encode("newPass")).thenReturn("$2a$newHash");
        when(jdbcTemplate.update(anyString(), anyString(), anyInt())).thenReturn(1);

        boolean result = userRepository.changePassword(1, "newPass");

        assertTrue(result);
    }

    @Test
    void changePassword_failure_returnsDbError() {
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$hash");
        when(jdbcTemplate.update(anyString(), anyString(), anyInt())).thenThrow(new RuntimeException("DB error"));

        boolean result = userRepository.changePassword(1, "newPass");

        assertFalse(result);
    }
}
