package com.org.controller.doctor;

import com.org.dao.AppointmentRepository;
import com.org.dao.DoctorRepository;
import com.org.entity.Doctor;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class DoctorControllerTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private HttpSession session;

    private DoctorController doctorController;
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        doctorController = new DoctorController(doctorRepository, appointmentRepository);
        redirectAttributes = new RedirectAttributesModelMap();
    }

    @Test
    void doctorLogin_success() {
        Doctor d = new Doctor();
        d.setId(1);
        when(doctorRepository.login("dr@x.com", "pass")).thenReturn(d);

        String view = doctorController.doctorLogin("dr@x.com", "pass", session, redirectAttributes);

        assertEquals("redirect:/doctor/index.jsp", view);
        verify(session).setAttribute("doctObj", d);
    }

    @Test
    void doctorLogin_invalidCredentials() {
        when(doctorRepository.login(anyString(), anyString())).thenReturn(null);

        String view = doctorController.doctorLogin("dr@x.com", "wrong", session, redirectAttributes);

        assertEquals("redirect:/doctor_login.jsp", view);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("errorMsg"));
    }

    @Test
    void doctorLogout_removesSession() {
        String view = doctorController.doctorLogout(session, redirectAttributes);

        assertEquals("redirect:/doctor_login.jsp", view);
        verify(session).removeAttribute("doctObj");
    }

    @Test
    void changePassword_oldPasswordMismatch() {
        when(doctorRepository.checkOldPassword(1, "oldPass")).thenReturn(false);

        String view = doctorController.changePassword(1, "oldPass", "newPass", session, redirectAttributes);

        assertEquals("redirect:/doctor/edit_profile.jsp", view);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("errorMsg"));
    }

    @Test
    void updateStatus_success() {
        when(appointmentRepository.updateCommentStatus(1, 2, "Approved")).thenReturn(true);

        String view = doctorController.updateAppointmentStatus(1, 2, "Approved", session, redirectAttributes);

        assertEquals("redirect:/doctor/patient.jsp", view);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("succMsg"));
    }
}
