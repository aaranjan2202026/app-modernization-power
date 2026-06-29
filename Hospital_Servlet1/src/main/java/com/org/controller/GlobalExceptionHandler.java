package com.org.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DataAccessException.class)
    public String handleDatabaseError(DataAccessException ex, RedirectAttributes ra) {
        log.error("Database error occurred", ex);
        ra.addFlashAttribute("errorMsg", "A database error occurred. Please try again.");
        return "redirect:/";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex, RedirectAttributes ra) {
        log.warn("Invalid argument: {}", ex.getMessage());
        ra.addFlashAttribute("errorMsg", "Invalid input provided. Please check your data.");
        return "redirect:/";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericError(Exception ex, RedirectAttributes ra) {
        log.error("Unexpected error occurred", ex);
        ra.addFlashAttribute("errorMsg", "An unexpected error occurred. Please try again.");
        return "redirect:/";
    }
}
