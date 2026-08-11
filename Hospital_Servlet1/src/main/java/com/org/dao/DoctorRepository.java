package com.org.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.org.entity.Doctor;

@Repository
public class DoctorRepository {

    private static final Logger log = LoggerFactory.getLogger(DoctorRepository.class);

    private static final String DOCTOR_COLS = "id, fullName, dob, qualification, specialist, email, mobNo, password";

    private static final RowMapper<Doctor> DOCTOR_ROW_MAPPER = (rs, rowNum) -> {
        Doctor d = new Doctor();
        d.setId(rs.getInt("id"));
        d.setFullName(rs.getString("fullName"));
        d.setDob(rs.getString("dob"));
        d.setQualification(rs.getString("qualification"));
        d.setSpecialist(rs.getString("specialist"));
        d.setEmail(rs.getString("email"));
        d.setMobNo(rs.getString("mobNo"));
        d.setPassword(rs.getString("password"));
        return d;
    };

    private final JdbcTemplate jdbcTemplate;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public DoctorRepository(JdbcTemplate jdbcTemplate, BCryptPasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean registerDoctor(Doctor d) {
        String sql = """
                INSERT INTO Doctor (fullName, dob, qualification, specialist, email, mobNo, password)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try {
            int i = jdbcTemplate.update(sql,
                    d.getFullName(),
                    java.sql.Date.valueOf(d.getDob()),
                    d.getQualification(),
                    d.getSpecialist(),
                    d.getEmail(),
                    d.getMobNo(),
                    passwordEncoder.encode(d.getPassword()));
            return i == 1;
        } catch (Exception e) {
            log.error("Error registering doctor: {}", e.getMessage(), e);
            return false;
        }
    }

    public List<Doctor> getAllDoctors() {
        String sql = "SELECT " + DOCTOR_COLS + " FROM Doctor ORDER BY id DESC";
        try {
            return jdbcTemplate.query(sql, DOCTOR_ROW_MAPPER);
        } catch (Exception e) {
            log.error("Error fetching all doctors: {}", e.getMessage(), e);
            return List.of();
        }
    }

    public Doctor getDoctorsById(int id) {
        String sql = "SELECT " + DOCTOR_COLS + " FROM Doctor WHERE id = ?";
        try {
            List<Doctor> list = jdbcTemplate.query(sql, DOCTOR_ROW_MAPPER, id);
            return list.isEmpty() ? null : list.getFirst();
        } catch (Exception e) {
            log.error("Error fetching doctor by id {}: {}", id, e.getMessage(), e);
            return null;
        }
    }

    public boolean updateDoctor(Doctor d) {
        String sql = """
                UPDATE Doctor
                SET fullName=?, dob=?, qualification=?, specialist=?, email=?, mobNo=?, password=?
                WHERE id=?
                """;
        try {
            int i = jdbcTemplate.update(sql,
                    d.getFullName(),
                    java.sql.Date.valueOf(d.getDob()),
                    d.getQualification(),
                    d.getSpecialist(),
                    d.getEmail(),
                    d.getMobNo(),
                    d.getPassword(),
                    d.getId());
            return i == 1;
        } catch (Exception e) {
            log.error("Error updating doctor {}: {}", d.getId(), e.getMessage(), e);
            return false;
        }
    }

    public boolean deleteDoctor(int id) {
        String sql = "DELETE FROM Doctor WHERE id = ?";
        try {
            int i = jdbcTemplate.update(sql, id);
            return i == 1;
        } catch (Exception e) {
            log.error("Error deleting doctor {}: {}", id, e.getMessage(), e);
            return false;
        }
    }

    public Doctor login(String email, String password) {
        String sql = "SELECT " + DOCTOR_COLS + " FROM Doctor WHERE email = ?";
        try {
            return jdbcTemplate.query(sql, rs -> {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    if (passwordEncoder.matches(password, storedHash)) {
                        Doctor d = new Doctor();
                        d.setId(rs.getInt("id"));
                        d.setFullName(rs.getString("fullName"));
                        d.setDob(rs.getString("dob"));
                        d.setQualification(rs.getString("qualification"));
                        d.setSpecialist(rs.getString("specialist"));
                        d.setEmail(rs.getString("email"));
                        d.setMobNo(rs.getString("mobNo"));
                        d.setPassword(storedHash);
                        return d;
                    }
                }
                return null;
            }, email);
        } catch (Exception e) {
            log.error("Error during doctor login: {}", e.getMessage(), e);
            return null;
        }
    }

    public int countDoctor() {
        return queryCount("SELECT COUNT(*) FROM Doctor");
    }

    public int countAppointment() {
        return queryCount("SELECT COUNT(*) FROM Appointment");
    }

    public int countAppointmentByDoctorId(int did) {
        String sql = "SELECT COUNT(*) FROM Appointment WHERE doctorId = ?";
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, did);
            return count != null ? count : 0;
        } catch (Exception e) {
            log.error("Error counting appointments for doctor {}: {}", did, e.getMessage(), e);
            return 0;
        }
    }

    public int countUser() {
        return queryCount("SELECT COUNT(*) FROM [user]");
    }

    public int countSpecialist() {
        return queryCount("SELECT COUNT(*) FROM Specalist");
    }

    private int queryCount(String sql) {
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
            return count != null ? count : 0;
        } catch (Exception e) {
            log.error("Error executing count query: {}", e.getMessage(), e);
            return 0;
        }
    }

    public boolean checkOldPassword(int userid, String oldPassword) {
        String sql = "SELECT password FROM Doctor WHERE id = ?";
        try {
            return jdbcTemplate.query(sql, rs -> {
                if (rs.next()) {
                    return passwordEncoder.matches(oldPassword, rs.getString("password"));
                }
                return false;
            }, userid);
        } catch (Exception e) {
            log.error("Error checking old password for doctor {}: {}", userid, e.getMessage(), e);
            return false;
        }
    }

    public boolean changePassword(int userid, String newPassword) {
        String sql = "UPDATE Doctor SET password = ? WHERE id = ?";
        try {
            int i = jdbcTemplate.update(sql, passwordEncoder.encode(newPassword), userid);
            return i == 1;
        } catch (Exception e) {
            log.error("Error changing password for doctor {}: {}", userid, e.getMessage(), e);
            return false;
        }
    }

    public boolean editDoctorProfile(Doctor d) {
        String sql = """
                UPDATE Doctor
                SET fullName=?, dob=?, qualification=?, specialist=?, email=?, mobNo=?
                WHERE id=?
                """;
        try {
            int i = jdbcTemplate.update(sql,
                    d.getFullName(),
                    java.sql.Date.valueOf(d.getDob()),
                    d.getQualification(),
                    d.getSpecialist(),
                    d.getEmail(),
                    d.getMobNo(),
                    d.getId());
            return i == 1;
        } catch (Exception e) {
            log.error("Error editing doctor profile {}: {}", d.getId(), e.getMessage(), e);
            return false;
        }
    }
}
