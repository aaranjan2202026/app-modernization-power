package com.org.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.org.entity.Appointment;
import com.org.helper.ConnectionHelper;

public class AppointmentDao {

    private Connection con = ConnectionHelper.getConObj();

    // ---------------- Add Appointment ----------------
    public boolean addAppointment(Appointment a) {
        boolean f = false;

        String sql = "INSERT INTO Appointment " +
                "(userId, fullName, gender, age, appoinDate, email, phNo, diseases, doctorId, address, [status]) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, a.getUserId());
            ps.setString(2, a.getFullName());
            ps.setString(3, a.getGender());
            ps.setString(4, a.getAge());

            // appoinDate is DATE in MSSQL (expecting "yyyy-MM-dd")
            ps.setDate(5, java.sql.Date.valueOf(a.getAppoinDate()));

            ps.setString(6, a.getEmail());
            ps.setString(7, a.getPhNo());
            ps.setString(8, a.getDiseases());
            ps.setInt(9, a.getDoctorId());
            ps.setString(10, a.getAddress());
            ps.setString(11, a.getStatus());

            int res = ps.executeUpdate();
            if (res == 1) {
                f = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return f;
    }

    // ---------------- Get Appointments by User ----------------
    public List<Appointment> getAllAppointmentByLoginUser(int userId) {
        List<Appointment> list = new ArrayList<>();

        String sql = "SELECT * FROM Appointment WHERE userId=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Appointment ap = extractAppointment(rs);
                    list.add(ap);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ---------------- Get Appointments by Doctor ----------------
    public List<Appointment> getAllAppointmentByDoctorLogin(int doctorId) {
        List<Appointment> list = new ArrayList<>();

        String sql = "SELECT * FROM Appointment WHERE doctorId=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, doctorId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Appointment ap = extractAppointment(rs);
                    list.add(ap);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ---------------- Get Appointment by ID ----------------
    public Appointment getAppointmentById(int id) {
        Appointment ap = null;

        String sql = "SELECT * FROM Appointment WHERE id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ap = extractAppointment(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ap;
    }

    // ---------------- Update Status/Comment ----------------
    public boolean updateCommentStatus(int id, int doctorId, String comm) {
        boolean f = false;

        String sql = "UPDATE Appointment SET [status]=? WHERE id=? AND doctorId=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, comm);
            ps.setInt(2, id);
            ps.setInt(3, doctorId);

            int i = ps.executeUpdate();
            if (i == 1) {
                f = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return f;
    }

    // ---------------- Get All Appointments ----------------
    public List<Appointment> getAllAppointment() {
        List<Appointment> list = new ArrayList<>();

        String sql = "SELECT * FROM Appointment ORDER BY id DESC";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Appointment ap = extractAppointment(rs);
                    list.add(ap);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ---------------- Utility Method to Map ResultSet ----------------
    private Appointment extractAppointment(ResultSet rs) throws Exception {
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
    }
}
