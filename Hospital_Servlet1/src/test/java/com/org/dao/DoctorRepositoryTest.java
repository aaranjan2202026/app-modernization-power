package com.org.dao;

import com.org.entity.Doctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class DoctorRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private DoctorRepository doctorRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        doctorRepository = new DoctorRepository(jdbcTemplate, passwordEncoder);
    }

    @Test
    void registerDoctor_success() {
        Doctor d = new Doctor("Dr. Smith", "1980-01-01", "MBBS", "General", "dr@x.com", "1234567890", "pass");
        when(passwordEncoder.encode("pass")).thenReturn("$2a$hash");
        when(jdbcTemplate.update(anyString(), anyString(), any(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(1);

        boolean result = doctorRepository.registerDoctor(d);

        assertTrue(result);
    }

    @Test
    void getAllDoctors_success() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(List.of(new Doctor()));

        List<Doctor> result = doctorRepository.getAllDoctors();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void getAllDoctors_dbError_returnsEmpty() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenThrow(new RuntimeException("DB error"));

        List<Doctor> result = doctorRepository.getAllDoctors();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void deleteDoctor_success() {
        when(jdbcTemplate.update(anyString(), anyInt())).thenReturn(1);

        boolean result = doctorRepository.deleteDoctor(1);

        assertTrue(result);
    }

    @Test
    void deleteDoctor_notFound() {
        when(jdbcTemplate.update(anyString(), anyInt())).thenReturn(0);

        boolean result = doctorRepository.deleteDoctor(999);

        assertFalse(result);
    }

    @Test
    void login_invalidCredentials_returnsNull() {
        when(jdbcTemplate.query(anyString(), any(ResultSetExtractor.class), anyString())).thenReturn(null);

        Doctor result = doctorRepository.login("unknown@x.com", "wrongpass");

        assertNull(result);
    }

    @Test
    void changePassword_success() {
        when(passwordEncoder.encode("newPass")).thenReturn("$2a$newHash");
        when(jdbcTemplate.update(anyString(), anyString(), anyInt())).thenReturn(1);

        boolean result = doctorRepository.changePassword(1, "newPass");

        assertTrue(result);
    }
}
