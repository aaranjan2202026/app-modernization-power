package com.org.controller.user;

import com.org.dao.UserRepository;
import com.org.entity.User;
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

class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpSession session;

    private UserController userController;
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userRepository);
        redirectAttributes = new RedirectAttributesModelMap();
    }

    @Test
    void userRegister_success() {
        when(userRepository.registerUser(any(User.class))).thenReturn(true);

        String view = userController.userRegister("John", "john@x.com", "pass", session, redirectAttributes);

        assertEquals("redirect:/signup.jsp", view);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("sucMsg"));
    }

    @Test
    void userRegister_failure() {
        when(userRepository.registerUser(any(User.class))).thenReturn(false);

        String view = userController.userRegister("John", "john@x.com", "pass", session, redirectAttributes);

        assertEquals("redirect:/signup.jsp", view);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("errorMsg"));
    }

    @Test
    void userLogin_success() {
        User u = new User("John", "john@x.com", "hashedPass");
        when(userRepository.Login("john@x.com", "pass")).thenReturn(u);

        String view = userController.userLogin("john@x.com", "pass", session, redirectAttributes);

        assertEquals("redirect:/index.jsp", view);
        verify(session).setAttribute("userObj", u);
    }

    @Test
    void userLogin_invalidCredentials() {
        when(userRepository.Login(anyString(), anyString())).thenReturn(null);

        String view = userController.userLogin("john@x.com", "wrong", session, redirectAttributes);

        assertEquals("redirect:/user_login.jsp", view);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("errorMsg"));
    }

    @Test
    void userLogout_removesSession() {
        String view = userController.userLogout(session, redirectAttributes);

        assertEquals("redirect:/user_login.jsp", view);
        verify(session).removeAttribute("userObj");
    }
}
