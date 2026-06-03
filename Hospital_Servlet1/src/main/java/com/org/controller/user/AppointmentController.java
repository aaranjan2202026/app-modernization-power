package com.org.controller.user;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.org.dao.AppointmentDao;
import com.org.dao.UserDao;
import com.org.entity.Appointment;

@Controller
public class AppointmentController {

    private static final String ERROR_MSG_ATTR = "errorMsg";

    @PostMapping("/appAppointment")
    public String addAppointment(@RequestParam("userid") int userId,
            @RequestParam String fullname,
            @RequestParam String gender,
            @RequestParam String age,
            @RequestParam("appoint_date") String appointDate,
            @RequestParam String email,
            @RequestParam String phno,
            @RequestParam String diseases,
            @RequestParam("doct") int doctorId,
            @RequestParam String address,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Appointment ap = new Appointment(userId, fullname, gender, age, appointDate, email, phno, diseases, doctorId,
                address, "Pending");

        AppointmentDao dao = new AppointmentDao();

        if (dao.addAppointment(ap)) {
            redirectAttributes.addFlashAttribute("succMsg", "Appointment Successfully");
        } else {
            redirectAttributes.addFlashAttribute(ERROR_MSG_ATTR, "Something wrong on server");
        }

        return "redirect:/user_appointment.jsp";
    }

    @PostMapping("/userChangePassword")
    public String changePassword(@RequestParam int uid,
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        UserDao dao = new UserDao();
        boolean res = dao.checkOldPassword(uid, oldPassword);

        if (res) {
            boolean updateRes = dao.changePassword(uid, newPassword);
            if (updateRes) {
                redirectAttributes.addFlashAttribute("sucMsg", "Password Change Successfully");
            } else {
                redirectAttributes.addFlashAttribute(ERROR_MSG_ATTR, "Something Wrong on Server");
            }
        } else {
            redirectAttributes.addFlashAttribute(ERROR_MSG_ATTR, "Old Password Incorrect");
        }

        return "redirect:/change_password.jsp";
    }
}