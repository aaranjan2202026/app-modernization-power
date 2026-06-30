package com.org.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.org.entity.User;
import com.org.helper.ConnectionHelper;

public class UserDao {

    public boolean registerUser(User u) {
        String sql = "INSERT INTO [user] (fullname, email, password) VALUES (?, ?, ?)";
        
        try (Connection con = ConnectionHelper.getConObj();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getFullname());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPassword());

            int res = ps.executeUpdate();
            if (res == 1) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public User Login(String email, String password) {
        User u = null;
        String sql = "SELECT id, fullname, email, password FROM [user] WHERE email = ? AND password = ?";
        
        try (Connection con = ConnectionHelper.getConObj();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    u = new User();
                    u.setId(rs.getInt("id"));
                    u.setFullname(rs.getString("fullname"));
                    u.setEmail(rs.getString("email"));
                    u.setPassword(rs.getString("password"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return u;
    }

    public boolean checkOldPassword(int userid, String oldPassword) {
        boolean f = false;
        String sql = "SELECT 1 FROM [user] WHERE id=? AND password=?";
        
        try (Connection con = ConnectionHelper.getConObj();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userid);
            ps.setString(2, oldPassword);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    f = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    public boolean changePassword(int userid, String newPassword) {
        boolean f = false;
        String sql = "UPDATE [user] SET password=? WHERE id=?";
        
        try (Connection con = ConnectionHelper.getConObj();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, userid);

            int i = ps.executeUpdate();
            if (i == 1) {
                f = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }
}
