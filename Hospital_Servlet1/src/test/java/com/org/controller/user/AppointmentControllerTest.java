package com.org.controller.user;

import com.org.dao.AppointmentRepository;
import com.org.dao.UserRepository;
import com.org.entity.Appointment;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AppointmentControllerTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpSession session;

    private AppointmentController appointmentController;
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        appointmentController = new AppointmentController(appointmentRepository, userRepository);
        redirectAttributes = new RedirectAttributesModelMap();
    }

    @Test
    void addAppointment_success() {
        when(appointmentRepository.addAppointment(any(Appointment.class))).thenReturn(true);

        String view = appointmentController.addAppointment(1, "John", "Male", "30",
                LocalDate.now(), "john@x.com", "1234", "Flu", 2, "123 St", session, redirectAttributes);

        assertEquals("redirect:/user_appointment.jsp", view);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("succMsg"));
    }

    @Test
    void addAppointment_failure() {
        when(appointmentRepository.addAppointment(any(Appointment.class))).thenReturn(false);

        String view = appointmentController.addAppointment(1, "John", "Male", "30",
                LocalDate.now(), "john@x.com", "1234", "Flu", 2, "123 St", session, redirectAttributes);

        assertEquals("redirect:/user_appointment.jsp", view);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("errorMsg"));
    }

    @Test
    void changePassword_oldPasswordMismatch() {
        when(userRepository.checkOldPassword(1, "oldPass")).thenReturn(false);

        String view = appointmentController.changePassword(1, "oldPass", "newPass", session, redirectAttributes);

        assertEquals("redirect:/change_password.jsp", view);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("errorMsg"));
    }

    @Test
    void changePassword_success() {
        when(userRepository.checkOldPassword(1, "oldPass")).thenReturn(true);
        when(userRepository.changePassword(1, "newPass")).thenReturn(true);

        String view = appointmentController.changePassword(1, "oldPass", "newPass", session, redirectAttributes);

        assertEquals("redirect:/change_password.jsp", view);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("sucMsg"));
    }
}
