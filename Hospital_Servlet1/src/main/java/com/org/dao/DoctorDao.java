package com.org.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.org.entity.Doctor;
import com.org.helper.ConnectionHelper;

@Repository
public class DoctorDao {

    // ---------------- Register Doctor ----------------
    public boolean registerDoctor(Doctor d) {
        boolean f = false;

        try (Connection con = ConnectionHelper.getConObj();
             PreparedStatement ps = con.prepareStatement(
                 "INSERT INTO Doctor (fullName, dob, qualification, specialist, email, mobNo, password) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                 
            ps.setString(1, d.getFullName());
            ps.setDate(2, java.sql.Date.valueOf(d.getDob()));
            ps.setString(3, d.getQualification());
            ps.setString(4, d.getSpecialist());
            ps.setString(5, d.getEmail());
            ps.setString(6, d.getMobNo());
            ps.setString(7, d.getPassword());

            int i = ps.executeUpdate();
            if (i == 1) {
                f = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return f;
    }

    // ---------------- Get All Doctors ----------------
    public List<Doctor> getAllDoctors() {
        List<Appointment> list = new ArrayList<>();
        Doctor d = null;
        try (Connection con = ConnectionHelper.getConObj();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM Doctor ORDER BY id DESC");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                d = extractDoctor(rs);
                list.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ---------------- Get Doctor by ID ----------------
    public Doctor getDoctorsById(int id) {
        Doctor d = null;
        try (Connection con = ConnectionHelper.getConObj();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM Doctor WHERE id=?")) {
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    d = extractDoctor(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return d;
    }

    // ---------------- Update Doctor ----------------
    public boolean updateDoctor(Doctor d) {
        boolean f = false;

        try (Connection con = ConnectionHelper.getConObj();
             PreparedStatement ps = con.prepareStatement(
                 "UPDATE Doctor SET fullName=?, dob=?, qualification=?, specialist=?, email=?, mobNo=?, password=? WHERE id=?")) {
                 
            ps.setString(1, d.getFullName());
            ps.setDate(2, java.sql.Date.valueOf(d.getDob()));
            ps.setString(3, d.getQualification());
            ps.setString(4, d.getSpecialist());
            ps.setString(5, d.getEmail());
            ps.setString(6, d.getMobNo());
            ps.setString(7, d.getPassword());
            ps.setInt(8, d.getId());

            int i = ps.executeUpdate();
            if (i == 1) {
                f = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return f;
    }

    // ---------------- Delete Doctor ----------------
    public boolean deleteDoctor(int id) {
        boolean f = false;

        try (Connection con = ConnectionHelper.getConObj();
             PreparedStatement ps = con.prepareStatement("DELETE FROM Doctor WHERE id=?")) {
            ps.setInt(1, id);
            int i = ps.executeUpdate();
            if (i == 1) {
                f = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    // ---------------- Doctor Login ----------------
    public Doctor login(String email, String password) {
        Doctor d = null;

        try (Connection con = ConnectionHelper.getConObj();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM Doctor WHERE email=? AND password=?")) {
            ps.setString(1, email);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    d = extractDoctor(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return d;
    }

    // ---------------- Counts ----------------
    public int countDoctor() {
        return count("SELECT COUNT(*) FROM Doctor");
    }

    public int countAppointment() {
        return count("SELECT COUNT(*) FROM Appointment");
    }

    public int countAppointmentByDocotrId(int did) {
        int i = 0;
        try (Connection con = ConnectionHelper.getConObj();
             PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM Appointment WHERE doctorId=?")) {
            ps.setInt(1, did);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    i = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    public int countUSer() {
        return count("SELECT COUNT(*) FROM [user]");
    }

    public int countSpecialist() {
        return count("SELECT COUNT(*) FROM Specalist");
    }

    private int count(String sql) {
        int i = 0;
        try (Connection con = ConnectionHelper.getConObj();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                i = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    // ---------------- Password Management ----------------
    public boolean checkOldPassword(int userid, String oldPassword) {
        boolean f = false;

        try (Connection con = ConnectionHelper.getConObj();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM Doctor WHERE id=? AND password=?")) {
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

        try (Connection con = ConnectionHelper.getConObj();
             PreparedStatement ps = con.prepareStatement("UPDATE Doctor SET password=? WHERE id=?")) {
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

    public boolean editDoctorProfile(Doctor d) {
        boolean f = false;

        try (Connection con = ConnectionHelper.getConObj();
             PreparedStatement ps = con.prepareStatement(
                 "UPDATE Doctor SET fullName=?, dob=?, qualification=?, specialist=?, email=?, mobNo=? WHERE id=?")) {
            ps.setString(1, d.getFullName());
            ps.setDate(2, java.sql.Date.valueOf(d.getDob()));
            ps.setString(3, d.getQualification());
            ps.setString(4, d.getSpecialist());
            ps.setString(5, d.getEmail());
            ps.setString(6, d.getMobNo());
            ps.setInt(7, d.getId());

            int i = ps.executeUpdate();
            if (i == 1) {
                f = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return f;
    }

    // ---------------- Utility ----------------
    private Doctor extractDoctor(ResultSet rs) throws Exception {
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
    }
}

