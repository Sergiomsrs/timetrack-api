package org.tfg.timetrackapi.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.tfg.timetrackapi.dto.UserDTO;
import org.tfg.timetrackapi.entity.Role;
import org.tfg.timetrackapi.services.TimeStampService;
import org.tfg.timetrackapi.services.UserService;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private TimeStampService timeStampService;

    @InjectMocks
    private UserController userController;

    @Test
    @DisplayName("Modificar datos usuario")
    public void testUpdateUser() {
        // Datos de entrada
        Long userId = 1L;
        UserDTO inputDto = new UserDTO();
        inputDto.setName("NuevoNombre");
        inputDto.setLastName("Apellido");
        inputDto.setDni("12345678A");
        inputDto.setRole(Role.valueOf("USER"));

        // Simulación del resultado esperado
        UserDTO updatedDto = new UserDTO();
        updatedDto.setName("NuevoNombre");
        updatedDto.setLastName("Apellido");
        updatedDto.setDni("12345678A");
        updatedDto.setRole(Role.valueOf("USER"));

        // Configurar el mock
        Mockito.when(userService.update(userId, inputDto)).thenReturn(updatedDto);

        // Ejecutar el método del controlador
        ResponseEntity<UserDTO> response = userController.updateUser(userId, inputDto);

        // Verificar el resultado
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("NuevoNombre", response.getBody().getName());
        Assertions.assertEquals("12345678A", response.getBody().getDni());
    }
}