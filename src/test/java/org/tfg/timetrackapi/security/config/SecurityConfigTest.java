package org.tfg.timetrackapi.security.config;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = true)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Un administrador puede acceder a la lista de usuarios")
    void userAccesAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user"))
                .andExpect(status().isOk());
    }

    @DisplayName("Un auditor puede acceder a la lista de usuarios")
    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void userAccesGuest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user"))
                .andExpect(status().isOk());
    }

    @DisplayName("Un usuario no puede acceder a la lista de usuarios")
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void userAccesUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user"))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Sin rol no se puede acceder a la lista de usuarios")
    @Test
    void userAccesNoRol() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user"))
                .andExpect(status().isForbidden());
    }
}
