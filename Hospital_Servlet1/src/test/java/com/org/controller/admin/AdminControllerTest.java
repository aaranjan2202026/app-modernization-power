package com.org.controller.admin;

import com.org.dao.DoctorRepository;
import com.org.dao.SpecialistRepository;
import com.org.entity.Doctor;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private SpecialistRepository specialistRepository;

    @Mock
    private HttpSession session;

    private AdminController adminController;
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adminController = new AdminController(doctorRepository, specialistRepository);
        ReflectionTestUtils.setField(adminController, "adminEmail", "admin@test.com");
        ReflectionTestUtils.setField(adminController, "adminPassword", "adminPass");
        redirectAttributes = new RedirectAttributesModelMap();
    }

    @Test
    void adminLogin_validCredentials() {
        String view = adminController.adminLogin("admin@test.com", "adminPass", session, redirectAttributes);

        assertEquals("redirect:/admin/index.jsp", view);
        verify(session).setAttribute(eq("adminObj"), any());
    }

    @Test
    void adminLogin_invalidCredentials() {
        String view = adminController.adminLogin("wrong@x.com", "bad", session, redirectAttributes);

        assertEquals("redirect:/admin_login.jsp", view);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("errorMsg"));
    }

    @Test
    void addSpecialist_success() {
        when(specialistRepository.addSpecialist("Cardiology")).thenReturn(true);

        String view = adminController.addSpecialist("Cardiology", session, redirectAttributes);

        assertEquals("redirect:/admin/index.jsp", view);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("sucMsg"));
    }

    @Test
    void addDoctor_success() {
        when(doctorRepository.registerDoctor(any(Doctor.class))).thenReturn(true);

        String view = adminController.addDoctor("Dr. Smith", "1980-01-01", "MBBS",
                "General", "dr@x.com", "1234", "pass", session, redirectAttributes);

        assertEquals("redirect:/admin/doctor.jsp", view);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("sucMsg"));
    }

    @Test
    void deleteDoctor_success() {
        when(doctorRepository.deleteDoctor(1)).thenReturn(true);

        String view = adminController.deleteDoctor(1, session, redirectAttributes);

        assertEquals("redirect:/admin/view_doctor.jsp", view);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("succMsg"));
    }
}
