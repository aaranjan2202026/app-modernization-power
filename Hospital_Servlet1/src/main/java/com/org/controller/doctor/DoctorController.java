package com.org.controller.doctor;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.org.dao.AppointmentRepository;
import com.org.dao.DoctorRepository;
import com.org.entity.Doctor;

@Controller
public class DoctorController {

    private static final String DOCTOR_OBJ_ATTR = DOCTOR_OBJ_ATTR;
    private static final String ERROR_MSG_ATTR = ERROR_MSG_ATTR;
    private static final String SUCCESS_MSG_ATTR = SUCCESS_MSG_ATTR;
    private static final String SERVER_ERROR_MSG = SERVER_ERROR_MSG;
    private static final String EDIT_PROFILE_REDIRECT = EDIT_PROFILE_REDIRECT;


    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public DoctorController(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @PostMapping("/doctorLogin")
    public String doctorLogin(@RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Doctor d = doctorRepository.login(email, password);

        if (d != null) {
            session.setAttribute(DOCTOR_OBJ_ATTR, d);
            return "redirect:/doctor/index.jsp";
        } else {
            redirectAttributes.addFlashAttribute(ERROR_MSG_ATTR, "invalid email or password");
            return "redirect:/doctor_login.jsp";
        }
    }

    @RequestMapping(value = "/doctorLogout", method = { RequestMethod.GET, RequestMethod.POST })
    public String doctorLogout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.removeAttribute(DOCTOR_OBJ_ATTR);
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
        boolean res = doctorRepository.editDoctorProfile(d);

        if (res) {
            Doctor updatedDoctor = doctorRepository.getDoctorsById(id);
            session.setAttribute(DOCTOR_OBJ_ATTR, updatedDoctor);
            redirectAttributes.addFlashAttribute(SUCCESS_MSG_ATTR, "Doctor Details Update Successfully");
        } else {
            redirectAttributes.addFlashAttribute(ERROR_MSG_ATTR, SERVER_ERROR_MSG);
        }

        return EDIT_PROFILE_REDIRECT;
    }

    @PostMapping("/doctorChangePassword")
    public String changePassword(@RequestParam int uid,
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        boolean res = doctorRepository.checkOldPassword(uid, oldPassword);

        if (res) {
            boolean updateRes = doctorRepository.changePassword(uid, newPassword);
            if (updateRes) {
                redirectAttributes.addFlashAttribute(SUCCESS_MSG_ATTR, "Password Change Successfully");
            } else {
                redirectAttributes.addFlashAttribute(ERROR_MSG_ATTR, SERVER_ERROR_MSG);
            }
        } else {
            redirectAttributes.addFlashAttribute(ERROR_MSG_ATTR, "Old Password Incorrect");
        }

        return EDIT_PROFILE_REDIRECT;
    }

    @PostMapping("/doctChangePassword")
    public String doctChangePassword(@RequestParam int uid,
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        boolean res = doctorRepository.checkOldPassword(uid, oldPassword);

        if (res) {
            boolean updateRes = doctorRepository.changePassword(uid, newPassword);
            if (updateRes) {
                redirectAttributes.addFlashAttribute("succMsg", "Password Change Successfully");
            } else {
                redirectAttributes.addFlashAttribute(ERROR_MSG_ATTR, SERVER_ERROR_MSG);
            }
        } else {
            redirectAttributes.addFlashAttribute(ERROR_MSG_ATTR, "Old Password Incorrect");
        }

        return EDIT_PROFILE_REDIRECT;
    }

    @PostMapping("/updateStatus")
    public String updateAppointmentStatus(@RequestParam int id,
            @RequestParam int did,
            @RequestParam String comm,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (appointmentRepository.updateCommentStatus(id, did, comm)) {
            redirectAttributes.addFlashAttribute("succMsg", "Comment Updated");
        } else {
            redirectAttributes.addFlashAttribute(ERROR_MSG_ATTR, SERVER_ERROR_MSG);
        }

        return "redirect:/doctor/patient.jsp";
    }
}
