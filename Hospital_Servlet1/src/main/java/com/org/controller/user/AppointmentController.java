package com.org.controller.user;

import java.time.LocalDate;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.org.dao.AppointmentRepository;
import com.org.dao.UserRepository;
import com.org.entity.Appointment;

@Controller
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    @Autowired
    public AppointmentController(AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/appAppointment")
    public String addAppointment(@RequestParam("userid") int userId,
            @RequestParam String fullname,
            @RequestParam String gender,
            @RequestParam String age,
            @RequestParam("appoint_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appointDate,
            @RequestParam String email,
            @RequestParam String phno,
            @RequestParam String diseases,
            @RequestParam("doct") int doctorId,
            @RequestParam String address,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Appointment ap = new Appointment(userId, fullname, gender, age, appointDate, email, phno, diseases, doctorId,
                address, "Pending");

        if (appointmentRepository.addAppointment(ap)) {
            redirectAttributes.addFlashAttribute("succMsg", "Appointment Successfully");
        } else {
            redirectAttributes.addFlashAttribute("errorMsg", "Something wrong on server");
        }

        return "redirect:/user_appointment.jsp";
    }

    @PostMapping("/userChangePassword")
    public String changePassword(@RequestParam int uid,
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        boolean res = userRepository.checkOldPassword(uid, oldPassword);

        if (res) {
            boolean updateRes = userRepository.changePassword(uid, newPassword);
            if (updateRes) {
                redirectAttributes.addFlashAttribute("sucMsg", "Password Change Successfully");
            } else {
                redirectAttributes.addFlashAttribute("errorMsg", "Something Wrong on Server");
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMsg", "Old Password Incorrect");
        }

        return "redirect:/change_password.jsp";
    }
}