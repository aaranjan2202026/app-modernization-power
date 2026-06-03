package com.org.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.org.dao.DoctorDao;
import com.org.dao.SpecialistDao;
import com.org.entity.Doctor;
import com.org.entity.Specalist;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/index.jsp")
    public String index() {
        return "index";
    }

    @GetMapping("/signup.jsp")
    public String signup() {
        return "signup";
    }

    @GetMapping("/user_login.jsp")
    public String userLogin() {
        return "user_login";
    }

    @GetMapping("/admin_login.jsp")
    public String adminLogin() {
        return "admin_login";
    }

    @GetMapping("/doctor_login.jsp")
    public String doctorLogin() {
        return "doctor_login";
    }

    @GetMapping("/user_appointment.jsp")
    public String userAppointment(Model model) {
        DoctorDao dao = new DoctorDao();
        List<Doctor> list = dao.getAllDoctors();
        model.addAttribute("doctorList", list);

        SpecialistDao specDao = new SpecialistDao();
        List<Specalist> specList = specDao.getAllSpecialist();
        model.addAttribute("specList", specList);

        return "user_appointment";
    }

    @GetMapping("/view_appointment.jsp")
    public String viewAppointment() {
        return "view_appointment";
    }

    @GetMapping("/change_password.jsp")
    public String changePassword() {
        return "change_password";
    }

    // Admin pages
    @GetMapping("/admin/index.jsp")
    public String adminIndex() {
        return "admin/index";
    }

    @GetMapping("/admin/doctor.jsp")
    public String adminDoctor(Model model) {
        SpecialistDao dao = new SpecialistDao();
        List<Specalist> list = dao.getAllSpecialist();
        model.addAttribute("specList", list);
        return "admin/doctor";
    }

    @GetMapping("/admin/view_doctor.jsp")
    public String adminViewDoctor(Model model) {
        DoctorDao dao = new DoctorDao();
        List<Doctor> list = dao.getAllDoctors();
        model.addAttribute("doctorList", list);
        return "admin/view_doctor";
    }

    @GetMapping("/admin/patient.jsp")
    public String adminPatient() {
        return "admin/patient";
    }

    // Doctor pages
    @GetMapping("/doctor/index.jsp")
    public String doctorIndex() {
        return "doctor/index";
    }

    @GetMapping("/doctor/patient.jsp")
    public String doctorPatient() {
        return "doctor/patient";
    }

    @GetMapping("/doctor/edit_profile.jsp")
    public String doctorEditProfile() {
        return "doctor/edit_profile";
    }

    @GetMapping("/doctor/comment.jsp")
    public String doctorComment() {
        return "doctor/comment";
    }

    @GetMapping("/about.jsp")
    public String about() {
        return "about";
    }
}