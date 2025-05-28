package org.tfg.timetrackapi.controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.MediaType;
import org.tfg.timetrackapi.entity.TimeStamp;
import org.tfg.timetrackapi.entity.User;
import org.tfg.timetrackapi.services.TimeStampService;
import org.tfg.timetrackapi.services.UserService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TimeStampControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TimeStampService timeStampService;

    @MockBean private UserService userService;

    @Test
    @DisplayName("Fichaje credenciales correctas")
    void successfulFicharReturnsOk() throws Exception {

        User mockUser = new User();
        mockUser.setId(1L);

        Mockito.when(userService.authenticateUser("1234", "1234")).thenReturn(mockUser);

        TimeStamp mockTimestamp = new TimeStamp();
        mockTimestamp.setId(100L);
        Mockito.when(timeStampService.addTimeStamp(mockUser)).thenReturn(mockTimestamp);

        String jsonRequest = """
    {
        "dni": "1234",
        "password": "1234"
    }
    """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/timestamp/fichar")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Fichaje credenciales incorrectas")
    void unsuccessfulFicharReturnsUnauthorized() throws Exception {
        // Simular que un usuario con credenciales incorrectas lanza una excepción
        Mockito.when(userService.authenticateUser("1234", "1235"))
                .thenThrow(new BadCredentialsException("Contraseña incorrecta"));

        String jsonRequest = """
    {
        "dni": "1234",
        "password": "1235"
    }
    """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/timestamp/fichar")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}