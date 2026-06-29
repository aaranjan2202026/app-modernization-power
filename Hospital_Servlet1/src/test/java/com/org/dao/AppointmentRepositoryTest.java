package com.org.dao;

import com.org.entity.Appointment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AppointmentRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private AppointmentRepository appointmentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        appointmentRepository = new AppointmentRepository(jdbcTemplate);
    }

    @Test
    void addAppointment_success() {
        Appointment a = new Appointment(1, "John", "Male", "30", LocalDate.now(),
                "john@x.com", "1234", "Flu", 1, "123 St", "Pending");
        when(jdbcTemplate.update(anyString(), anyInt(), anyString(), anyString(), anyString(),
                any(), anyString(), anyString(), anyString(), anyInt(), anyString(), anyString())).thenReturn(1);

        boolean result = appointmentRepository.addAppointment(a);

        assertTrue(result);
    }

    @Test
    void addAppointment_failure_returnsDbError() {
        Appointment a = new Appointment(1, "John", "Male", "30", LocalDate.now(),
                "john@x.com", "1234", "Flu", 1, "123 St", "Pending");
        when(jdbcTemplate.update(anyString(), anyInt(), anyString(), anyString(), anyString(),
                any(), anyString(), anyString(), anyString(), anyInt(), anyString(), anyString()))
                .thenThrow(new RuntimeException("DB error"));

        boolean result = appointmentRepository.addAppointment(a);

        assertFalse(result);
    }

    @Test
    void getAllAppointmentByLoginUser_success() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyInt())).thenReturn(List.of(new Appointment()));

        List<Appointment> result = appointmentRepository.getAllAppointmentByLoginUser(1);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void getAllAppointmentByDoctorLogin_dbError_returnsEmpty() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyInt())).thenThrow(new RuntimeException("DB"));

        List<Appointment> result = appointmentRepository.getAllAppointmentByDoctorLogin(1);

        assertTrue(result.isEmpty());
    }

    @Test
    void updateCommentStatus_success() {
        when(jdbcTemplate.update(anyString(), anyString(), anyInt(), anyInt())).thenReturn(1);

        boolean result = appointmentRepository.updateCommentStatus(1, 1, "Approved");

        assertTrue(result);
    }

    @Test
    void getAllAppointment_success() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(List.of(new Appointment()));

        List<Appointment> result = appointmentRepository.getAllAppointment();

        assertNotNull(result);
    }
}
