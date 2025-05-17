package org.tfg.timetrackapi.services;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.tfg.timetrackapi.entity.User;
import org.tfg.timetrackapi.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    public void testGetById_UserExists_ReturnsUser() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("Juan");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.getById(userId);

        assertNotNull(result);
        assertEquals("Juan", result.getName());
    }

    @Test
    public void testGetById_UserDoesNotExist_ThrowsException() {
        Long userId = 2L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.getById(userId);
        });

        assertEquals("Usuario no encontrado con ID: " + userId, exception.getMessage());
    }

    // 🔐 Prueba 1: usuario y contraseña correctos
    @Test
    void authenticateUser_validCredentials_returnsUser() {
        String dni = "12345678A";
        String rawPassword = "password";
        String encodedPassword = "$2a$10$encoded";

        User user = new User();
        user.setDni(dni);
        user.setPassword(encodedPassword);

        when(userRepository.findByDni(dni)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        User result = userService.authenticateUser(dni, rawPassword);

        assertNotNull(result);
        assertEquals(dni, result.getDni());
    }

    // ❌ Prueba 2: usuario no existe
    @Test
    void authenticateUser_userNotFound_throwsUsernameNotFoundException() {
        String dni = "99999999Z";
        when(userRepository.findByDni(dni)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.authenticateUser(dni, "password");
        });
    }

    // ❌ Prueba 3: contraseña incorrecta
    @Test
    void authenticateUser_wrongPassword_throwsBadCredentialsException() {
        String dni = "12345678A";
        String rawPassword = "wrongPassword";
        String encodedPassword = "$2a$10$encoded";

        User user = new User();
        user.setDni(dni);
        user.setPassword(encodedPassword);

        when(userRepository.findByDni(dni)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> {
            userService.authenticateUser(dni, rawPassword);
        });
    }
}
