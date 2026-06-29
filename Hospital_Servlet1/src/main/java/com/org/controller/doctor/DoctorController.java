package com.org.controller.doctor;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.org.dao.DoctorDao;
import com.org.entity.Doctor;

@Controller
public class DoctorController {

    @PostMapping("/doctorLogin")
    public String doctorLogin(@RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        DoctorDao dao = new DoctorDao();
        Doctor d = dao.login(email, password);

        if (d != null) {
            session.setAttribute("doctObj", d);
            return "redirect:/doctor/index.jsp";
        } else {
            redirectAttributes.addFlashAttribute("errorMsg", "invalid email or password");
            return "redirect:/doctor_login.jsp";
        }
    }

    @RequestMapping(value = "/doctorLogout", method = { RequestMethod.GET, RequestMethod.POST })
    public String doctorLogout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.removeAttribute("doctObj");
        redirectAttributes.addFlashAttribute("sucMsg", "Doctor Logout Successfully");
        return "redirect:/doctor_login.jsp";
    }

    @PostMapping("/doctorUpdateProfile")
    public String updateProfile(@RequestParam String fullname,
            @RequestParam String dob,
            @RequestParam String qualification,
            @RequestParam String spec,
            @RequestParam String email,
            @RequestParam String mobno,
            @RequestParam int id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Doctor d = new Doctor(id, fullname, dob, qualification, spec, email, mobno, "");
        DoctorDao dao = new DoctorDao();
        boolean res = dao.editDoctorProfile(d);

        if (res) {
            Doctor updateDoctor = dao.getDoctorsById(id);
            session.setAttribute("doctObj", updateDoctor);
            redirectAttributes.addFlashAttribute("sucMsg", "Doctor Details Update Successfully");
        } else {
            redirectAttributes.addFlashAttribute("errorMsg", "Something Wrong on Server");
        }

        return "redirect:/doctor/edit_profile.jsp";
    }

    @PostMapping("/doctorChangePassword")
    public String changePassword(@RequestParam int uid,
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        DoctorDao dao = new DoctorDao();
        boolean res = dao.checkOldPassword(uid, oldPassword);

        if (res) {
            boolean updateRes = dao.changePassword(uid, newPassword);
            if (updateRes) {
                redirectAttributes.addFlashAttribute("sucMsg", "Password Change Successfully");
            } else {
                redirectAttributes.addFlashAttribute("errorMsg", "Something Wrong on Server");
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMsg", "Old Password Incorrect");
        }

        return "redirect:/doctor/edit_profile.jsp";
    }

    @PostMapping("/doctChangePassword")
    public String doctChangePassword(@RequestParam int uid,
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        DoctorDao dao = new DoctorDao();
        boolean res = dao.checkOldPassword(uid, oldPassword);

        if (res) {
            boolean updateRes = dao.changePassword(uid, newPassword);
            if (updateRes) {
                redirectAttributes.addFlashAttribute("succMsg", "Password Change Successfully");
            } else {
                redirectAttributes.addFlashAttribute("errorMsg", "Something Wrong on Server");
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMsg", "Old Password Incorrect");
        }

        return "redirect:/doctor/edit_profile.jsp";
    }

    @PostMapping("/updateStatus")
    public String updateAppointmentStatus(@RequestParam int id,
            @RequestParam int did,
            @RequestParam String comm,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        com.org.dao.AppointmentDao dao = new com.org.dao.AppointmentDao();

        if (dao.updateCommentStatus(id, did, comm)) {
            redirectAttributes.addFlashAttribute("succMsg", "Comment Updated");
        } else {
            redirectAttributes.addFlashAttribute("errorMsg", "Something wrong on server");
        }

        return "redirect:/doctor/patient.jsp";
    }
}