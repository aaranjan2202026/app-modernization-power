package com.org.dao;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.org.entity.Appointment;

@Repository
public class AppointmentRepository {

    private static final Logger log = LoggerFactory.getLogger(AppointmentRepository.class);

    private static final String APPT_COLS =
            "id, userId, fullName, gender, age, appoinDate, email, phNo, diseases, doctorId, address, [status]";

    private static final RowMapper<Appointment> APPT_ROW_MAPPER = (rs, rowNum) -> {
        Appointment ap = new Appointment();
        ap.setId(rs.getInt("id"));
        ap.setUserId(rs.getInt("userId"));
        ap.setFullName(rs.getString("fullName"));
        ap.setGender(rs.getString("gender"));
        ap.setAge(rs.getString("age"));
        java.sql.Date sqlDate = rs.getDate("appoinDate");
        ap.setAppoinDate(sqlDate != null ? sqlDate.toLocalDate() : null);
        ap.setEmail(rs.getString("email"));
        ap.setPhNo(rs.getString("phNo"));
        ap.setDiseases(rs.getString("diseases"));
        ap.setDoctorId(rs.getInt("doctorId"));
        ap.setAddress(rs.getString("address"));
        ap.setStatus(rs.getString("status"));
        return ap;
    };

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AppointmentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean addAppointment(Appointment a) {
        String sql = "INSERT INTO Appointment " +
                "(userId, fullName, gender, age, appoinDate, email, phNo, diseases, doctorId, address, [status]) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            int res = jdbcTemplate.update(sql,
                    a.getUserId(),
                    a.getFullName(),
                    a.getGender(),
                    a.getAge(),
                    a.getAppoinDate() != null ? java.sql.Date.valueOf(a.getAppoinDate()) : null,
                    a.getEmail(),
                    a.getPhNo(),
                    a.getDiseases(),
                    a.getDoctorId(),
                    a.getAddress(),
                    a.getStatus());
            return res == 1;
        } catch (Exception e) {
            log.error("Error adding appointment: {}", e.getMessage(), e);
            return false;
        }
    }

    public List<Appointment> getAllAppointmentByLoginUser(int userId) {
        String sql = "SELECT " + APPT_COLS + " FROM Appointment WHERE userId = ?";
        try {
            return jdbcTemplate.query(sql, APPT_ROW_MAPPER, userId);
        } catch (Exception e) {
            log.error("Error fetching appointments for user {}: {}", userId, e.getMessage(), e);
            return List.of();
        }
    }

    public List<Appointment> getAllAppointmentByDoctorLogin(int doctorId) {
        String sql = "SELECT " + APPT_COLS + " FROM Appointment WHERE doctorId = ?";
        try {
            return jdbcTemplate.query(sql, APPT_ROW_MAPPER, doctorId);
        } catch (Exception e) {
            log.error("Error fetching appointments for doctor {}: {}", doctorId, e.getMessage(), e);
            return List.of();
        }
    }

    public Appointment getAppointmentById(int id) {
        String sql = "SELECT " + APPT_COLS + " FROM Appointment WHERE id = ?";
        try {
            List<Appointment> list = jdbcTemplate.query(sql, APPT_ROW_MAPPER, id);
            return list.isEmpty() ? null : list.get(0);
        } catch (Exception e) {
            log.error("Error fetching appointment by id {}: {}", id, e.getMessage(), e);
            return null;
        }
    }

    public boolean updateCommentStatus(int id, int doctorId, String comm) {
        String sql = "UPDATE Appointment SET [status] = ? WHERE id = ? AND doctorId = ?";
        try {
            int i = jdbcTemplate.update(sql, comm, id, doctorId);
            return i == 1;
        } catch (Exception e) {
            log.error("Error updating appointment status for id {}: {}", id, e.getMessage(), e);
            return false;
        }
    }

    public List<Appointment> getAllAppointment() {
        String sql = "SELECT " + APPT_COLS + " FROM Appointment ORDER BY id DESC";
        try {
            return jdbcTemplate.query(sql, APPT_ROW_MAPPER);
        } catch (Exception e) {
            log.error("Error fetching all appointments: {}", e.getMessage(), e);
            return List.of();
        }
    }
}
