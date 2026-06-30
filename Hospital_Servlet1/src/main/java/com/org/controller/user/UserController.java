package com.org.controller.user;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.org.dao.UserRepository;
import com.org.entity.User;

@Controller
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/user_register")
    public String userRegister(@RequestParam String fullname,
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User u = new User(fullname, email, password);
        boolean res = userRepository.registerUser(u);

        if (res) {
            redirectAttributes.addFlashAttribute("sucMsg", "SIGNUP SUCCESS");
        } else {
            redirectAttributes.addFlashAttribute("errorMsg", "SIGNUP FAILED");
        }

        return "redirect:/signup.jsp";
    }

    @PostMapping("/userLogin")
    public String userLogin(@RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User u = userRepository.Login(email, password);

        if (u != null) {
            session.setAttribute("userObj", u);
            return "redirect:/index.jsp";
        } else {
            redirectAttributes.addFlashAttribute("errorMsg", "invalid user or password");
            return "redirect:/user_login.jsp";
        }
    }

    @RequestMapping(value = "/userLogout", method = { RequestMethod.GET, RequestMethod.POST })
    public String userLogout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.removeAttribute("userObj");
        redirectAttributes.addFlashAttribute("sucMsg", "User Logout Successfully");
        return "redirect:/user_login.jsp";
    }
}