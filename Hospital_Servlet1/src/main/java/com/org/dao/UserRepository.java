package com.org.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.org.entity.User;

@Repository
public class UserRepository {

    private static final Logger log = LoggerFactory.getLogger(UserRepository.class);

    private final JdbcTemplate jdbcTemplate;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate, BCryptPasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean registerUser(User u) {
        String sql = "INSERT INTO [user] (fullname, email, password) VALUES (?, ?, ?)";
        try {
            int res = jdbcTemplate.update(sql, u.getFullname(), u.getEmail(), passwordEncoder.encode(u.getPassword()));
            return res == 1;
        } catch (Exception e) {
            log.error("Error registering user: {}", e.getMessage(), e);
            return false;
        }
    }

    public User Login(String email, String password) {
        String sql = "SELECT id, fullname, email, password FROM [user] WHERE email = ?";
        try {
            return jdbcTemplate.query(sql, rs -> {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    if (passwordEncoder.matches(password, storedHash)) {
                        User u = new User();
                        u.setId(rs.getInt("id"));
                        u.setFullname(rs.getString("fullname"));
                        u.setEmail(rs.getString("email"));
                        u.setPassword(storedHash);
                        return u;
                    }
                }
                return null;
            }, email);
        } catch (Exception e) {
            log.error("Error during user login: {}", e.getMessage(), e);
            return null;
        }
    }

    public boolean checkOldPassword(int userid, String oldPassword) {
        String sql = "SELECT password FROM [user] WHERE id = ?";
        try {
            return jdbcTemplate.query(sql, rs -> {
                if (rs.next()) {
                    return passwordEncoder.matches(oldPassword, rs.getString("password"));
                }
                return false;
            }, userid);
        } catch (Exception e) {
            log.error("Error checking old password for user {}: {}", userid, e.getMessage(), e);
            return false;
        }
    }

    public boolean changePassword(int userid, String newPassword) {
        String sql = "UPDATE [user] SET password = ? WHERE id = ?";
        try {
            int i = jdbcTemplate.update(sql, passwordEncoder.encode(newPassword), userid);
            return i == 1;
        } catch (Exception e) {
            log.error("Error changing password for user {}: {}", userid, e.getMessage(), e);
            return false;
        }
    }
}
