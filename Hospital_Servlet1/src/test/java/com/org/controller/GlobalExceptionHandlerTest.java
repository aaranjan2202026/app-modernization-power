package com.org.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        redirectAttributes = new RedirectAttributesModelMap();
    }

    @Test
    void handleDatabaseError_setsErrorMessageAndRedirects() {
        DataAccessException ex = new DataAccessResourceFailureException("DB down");

        String view = handler.handleDatabaseError(ex, redirectAttributes);

        assertEquals("redirect:/", view);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("errorMsg"));
    }

    @Test
    void handleIllegalArgument_setsErrorMessageAndRedirects() {
        IllegalArgumentException ex = new IllegalArgumentException("Bad input");

        String view = handler.handleIllegalArgument(ex, redirectAttributes);

        assertEquals("redirect:/", view);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("errorMsg"));
    }

    @Test
    void handleGenericError_setsErrorMessageAndRedirects() {
        Exception ex = new RuntimeException("Unexpected");

        String view = handler.handleGenericError(ex, redirectAttributes);

        assertEquals("redirect:/", view);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("errorMsg"));
    }
}
