package com.org.controller.admin;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.org.dao.DoctorDao;
import com.org.dao.SpecialistDao;
import com.org.entity.Doctor;
import com.org.entity.Specalist;
import com.org.entity.User;

@Controller
public class AdminController {

    private static final String ERROR_MSG_ATTR = "errorMsg";
    private static final String SUCCESS_MSG_ATTR = "sucMsg";
    private static final String ADMIN_LOGIN_REDIRECT = "redirect:/admin_login.jsp";

    @PostMapping("/adminLogin")
    public String adminLogin(@RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if ("admin@gmail.com".equals(email) && "admin".equals(password)) {
            session.setAttribute("adminObj", new User());
            return "redirect:/admin/index.jsp";
        } else {
            redirectAttributes.addFlashAttribute(ERROR_MSG_ATTR, "invalid user or password");
            return ADMIN_LOGIN_REDIRECT;
        }
    }

    @RequestMapping(value = "/adminLogout", method = { RequestMethod.GET, RequestMethod.POST })
    public String adminLogout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.removeAttribute("adminObj");
        redirectAttributes.addFlashAttribute(SUCCESS_MSG_ATTR, "Admin Logout Successfully");
        return ADMIN_LOGIN_REDIRECT;
    }

    @PostMapping("/addSpecialist")
    public String addSpecialist(@RequestParam String specName,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        SpecialistDao dao = new SpecialistDao();
        boolean res = dao.addSpecialist(specName);

        if (res) {
            redirectAttributes.addFlashAttribute(SUCCESS_MSG_ATTR, "Specialist Added Successfully");
        } else {
            redirectAttributes.addFlashAttribute(ERROR_MSG_ATTR, "Something Wrong on Server");
        }

        return "redirect:/admin/index.jsp";
    }

    @PostMapping("/addDoctor")
    public String addDoctor(@RequestParam String fullname,
            @RequestParam String dob,
            @RequestParam String qualification,
            @RequestParam String spec,
            @RequestParam String email,
            @RequestParam String mobno,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Doctor d = new Doctor(fullname, dob, qualification, spec, email, mobno, password);
        DoctorDao dao = new DoctorDao();
        boolean res = dao.registerDoctor(d);

        if (res) {
            redirectAttributes.addFlashAttribute(SUCCESS_MSG_ATTR, "Doctor Added Successfully");
        } else {
            redirectAttributes.addFlashAttribute(ERROR_MSG_ATTR, "Something Wrong on Server");
        }

        return "redirect:/admin/doctor.jsp";
    }

    @PostMapping("/updateDoctor")
    public String updateDoctor(@RequestParam String fullname,
            @RequestParam String dob,
            @RequestParam String qualification,
            @RequestParam String spec,
            @RequestParam String email,
            @RequestParam String mobno,
            @RequestParam String password,
            @RequestParam int id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Doctor d = new Doctor(id, fullname, dob, qualification, spec, email, mobno, password);
        DoctorDao dao = new DoctorDao();
        boolean res = dao.updateDoctor(d);

        if (res) {
            redirectAttributes.addFlashAttribute("succMsg", "Doctor Updated Successfully");
        } else {
            redirectAttributes.addFlashAttribute(ERROR_MSG_ATTR, "Something Went Wrong");
        }

        return "redirect:/admin/view_doctor.jsp";
    }

    @RequestMapping(value = "/deleteDoctor", method = { RequestMethod.GET, RequestMethod.POST })
    public String deleteDoctor(@RequestParam int id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        DoctorDao dao = new DoctorDao();
        boolean res = dao.deleteDoctor(id);

        if (res) {
            redirectAttributes.addFlashAttribute("succMsg", "Doctor Deleted Successfully");
        } else {
            redirectAttributes.addFlashAttribute(ERROR_MSG_ATTR, "Something Went Wrong");
        }

        return "redirect:/admin/view_doctor.jsp";
    }
}