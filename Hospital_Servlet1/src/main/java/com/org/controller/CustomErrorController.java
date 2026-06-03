package com.org.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object errorMessage = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object requestUri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            model.addAttribute("statusCode", statusCode);

            switch (statusCode) {
                case 404:
                    model.addAttribute("errorTitle", "Page Not Found");
                    model.addAttribute("errorMessage", "The page you are looking for does not exist.");
                    break;
                case 405:
                    model.addAttribute("errorTitle", "Method Not Allowed");
                    model.addAttribute("errorMessage", "The request method is not allowed for this resource.");
                    break;
                case 500:
                    model.addAttribute("errorTitle", "Internal Server Error");
                    model.addAttribute("errorMessage", "An internal server error occurred.");
                    break;
                default:
                    model.addAttribute("errorTitle", "Error " + statusCode);
                    model.addAttribute("errorMessage", "An error occurred while processing your request.");
                    break;
            }
        }

        model.addAttribute("requestUri", requestUri);
        model.addAttribute("originalError", errorMessage);

        // Return to a simple error page or redirect to home
        return "redirect:/";
    }
}