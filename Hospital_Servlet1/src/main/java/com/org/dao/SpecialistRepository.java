package com.org.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.org.entity.Specalist;

@Repository
public class SpecialistRepository {

    private static final Logger log = LoggerFactory.getLogger(SpecialistRepository.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SpecialistRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean addSpecialist(String name) {
        String sql = "INSERT INTO specalist(specialistName) VALUES(?)";
        try {
            int i = jdbcTemplate.update(sql, name);
            return i == 1;
        } catch (Exception e) {
            log.error("Error adding specialist: {}", e.getMessage(), e);
            return false;
        }
    }

    public List<Specalist> getAllSpecialist() {
        String sql = "SELECT id, specialistName FROM specalist";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                Specalist s = new Specalist();
                s.setId(rs.getInt("id"));
                s.setSpecialistName(rs.getString("specialistName"));
                return s;
            });
        } catch (Exception e) {
            log.error("Error fetching all specialists: {}", e.getMessage(), e);
            return List.of();
        }
    }
}
