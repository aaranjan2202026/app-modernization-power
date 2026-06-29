package com.org.dao;

import com.org.entity.Specalist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SpecialistRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private SpecialistRepository specialistRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        specialistRepository = new SpecialistRepository(jdbcTemplate);
    }

    @Test
    void addSpecialist_success() {
        when(jdbcTemplate.update(anyString(), anyString())).thenReturn(1);

        boolean result = specialistRepository.addSpecialist("Cardiology");

        assertTrue(result);
    }

    @Test
    void addSpecialist_failure() {
        when(jdbcTemplate.update(anyString(), anyString())).thenReturn(0);

        boolean result = specialistRepository.addSpecialist("Cardiology");

        assertFalse(result);
    }

    @Test
    void addSpecialist_dbError_returnsFalse() {
        when(jdbcTemplate.update(anyString(), anyString())).thenThrow(new RuntimeException("DB error"));

        boolean result = specialistRepository.addSpecialist("Cardiology");

        assertFalse(result);
    }

    @Test
    void getAllSpecialist_success() {
        Specalist s = new Specalist();
        s.setId(1);
        s.setSpecialistName("Cardiology");
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(List.of(s));

        List<Specalist> result = specialistRepository.getAllSpecialist();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cardiology", result.get(0).getSpecialistName());
    }

    @Test
    void getAllSpecialist_dbError_returnsEmpty() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenThrow(new RuntimeException("DB error"));

        List<Specalist> result = specialistRepository.getAllSpecialist();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
