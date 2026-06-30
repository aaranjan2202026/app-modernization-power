package com.org.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.org.entity.Specalist;
import com.org.helper.ConnectionHelper;

public class SpecialistDao {

    private Connection con = ConnectionHelper.getConObj();

    // Add specialist
    public boolean addSpecialist(String name) {
        boolean f = false;
        String sql = "INSERT INTO specalist(specialistName) VALUES(?)";
        
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            int i = ps.executeUpdate();
            if (i == 1) {
                f = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    // Get all specialists
    public List<Specalist> getAllSpecialist() {
        List<Specalist> list = new ArrayList<>();
        String sql = "SELECT * FROM specalist";
        
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Specalist s = new Specalist();
                s.setId(rs.getInt("id"));
                s.setSpecialistName(rs.getString("specialistName"));
                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
