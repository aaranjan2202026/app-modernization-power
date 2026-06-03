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

    private static final String DOCTOR_SESSION_ATTR = "doctObj";
    private static final String ERROR_MSG_ATTR = "errorMsg";
    private static final String SUCCESS_MSG_ATTR = "sucMsg";
    private static final String SERVER_ERROR_MSG = "Something Wrong on Server";
    private static final String DOCTOR_EDIT_PROFILE_REDIRECT = "redirect:/doctor/edit_profile.jsp";

    @PostMapping("/doctorLogin")
    public String doctorLogin(@RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        DoctorDao dao = new DoctorDao();
        Doctor d = dao.login(email, password);

        if (d != null) {
            session.setAttribute(DOCTOR_SESSION_ATTR, d);
            return "redirect:/doctor/index.jsp";
        } else {
            redirectAttributes.addFlashAttribute(ERROR_MSG_ATTR, "invalid email or password");
            return "redirect:/doctor_login.jsp";
        }
    }

    @RequestMapping(value = "/doctorLogout", method = { RequestMethod.GET, RequestMethod.POST })
    public String doctorLogout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.removeAttribute(DOCTOR_SESSION_ATTR);
        redirectAttributes.addFlashAttribute(SUCCESS_MSG_ATTR, "Doctor Logout Successfully");
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
            session.setAttribute(DOCTOR_SESSION_ATTR, updateDoctor);
            redirectAttributes.addFlashAttribute(SUCCESS_MSG_ATTR, "Doctor Details Update Successfully");
        } else {
            redirectAttributes.addFlashAttribute(ERROR_MSG_ATTR, SERVER_ERROR_MSG);
        }

        return DOCTOR_EDIT_PROFILE_REDIRECT;
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
                redirectAttributes.addFlashAttribute(SUCCESS_MSG_ATTR, "Password Change Successfully");
            } else {
                redirectAttributes.addFlashAttribute(ERROR_MSG_ATTR, SERVER_ERROR_MSG);
            }
        } else {
            redirectAttributes.addFlashAttribute(ERROR_MSG_ATTR, "Old Password Incorrect");
        }

        return DOCTOR_EDIT_PROFILE_REDIRECT;
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
                redirectAttributes.addFlashAttribute(ERROR_MSG_ATTR, SERVER_ERROR_MSG);
            }
        } else {
            redirectAttributes.addFlashAttribute(ERROR_MSG_ATTR, "Old Password Incorrect");
        }

        return DOCTOR_EDIT_PROFILE_REDIRECT;
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
            redirectAttributes.addFlashAttribute(ERROR_MSG_ATTR, "Something wrong on server");
        }

        return "redirect:/doctor/patient.jsp";
    }
}