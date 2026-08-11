package com.org.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.org.dao.DoctorRepository;
import com.org.dao.SpecialistRepository;
import com.org.entity.Doctor;
import com.org.entity.Specalist;

@Controller
public class HomeController {

    private final DoctorRepository doctorRepository;
    private final SpecialistRepository specialistRepository;

    public HomeController(DoctorRepository doctorRepository, SpecialistRepository specialistRepository) {
        this.doctorRepository = doctorRepository;
        this.specialistRepository = specialistRepository;
    }

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
        List<Doctor> list = doctorRepository.getAllDoctors();
        model.addAttribute("doctorList", list);

        List<Specalist> specList = specialistRepository.getAllSpecialist();
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
        List<Specalist> list = specialistRepository.getAllSpecialist();
        model.addAttribute("specList", list);
        return "admin/doctor";
    }

    @GetMapping("/admin/view_doctor.jsp")
    public String adminViewDoctor(Model model) {
        List<Doctor> list = doctorRepository.getAllDoctors();
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