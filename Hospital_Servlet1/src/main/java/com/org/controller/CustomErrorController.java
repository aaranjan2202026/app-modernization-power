package com.org.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    private static final String ERROR_TITLE_ATTR = "errorTitle";
    private static final String ERROR_MESSAGE_ATTR = "errorMessage";

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
                    model.addAttribute(ERROR_TITLE_ATTR, "Page Not Found");
                    model.addAttribute(ERROR_MESSAGE_ATTR, "The page you are looking for does not exist.");
                    break;
                case 405:
                    model.addAttribute(ERROR_TITLE_ATTR, "Method Not Allowed");
                    model.addAttribute(ERROR_MESSAGE_ATTR, "The request method is not allowed for this resource.");
                    break;
                case 500:
                    model.addAttribute(ERROR_TITLE_ATTR, "Internal Server Error");
                    model.addAttribute(ERROR_MESSAGE_ATTR, "An internal server error occurred.");
                    break;
                default:
                    model.addAttribute(ERROR_TITLE_ATTR, "Error " + statusCode);
                    model.addAttribute(ERROR_MESSAGE_ATTR, "An error occurred while processing your request.");
                    break;
            }
        }

        model.addAttribute("requestUri", requestUri);
        model.addAttribute("originalError", errorMessage);

        // Return to a simple error page or redirect to home
        return "redirect:/";
    }
}
