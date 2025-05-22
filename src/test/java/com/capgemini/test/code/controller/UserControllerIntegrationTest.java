package com.capgemini.test.code.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.capgemini.test.code.clients.CheckDniResponse;
import com.capgemini.test.code.clients.DniClient;
import com.capgemini.test.code.dto.CreateUserRequest;
import com.capgemini.test.code.entity.Role;
import com.capgemini.test.code.entity.Sala;
import com.capgemini.test.code.entity.User;
import com.capgemini.test.code.repository.SalaRepository;
import com.capgemini.test.code.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DniClient dniClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SalaRepository salaRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        salaRepository.save(new Sala(1L));
    }

    @Test
    void testCreateUserOk() throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("pablo");
        request.setEmail("email@email.com");
        request.setPhone("677998899");
        request.setRol("admin");
        request.setDni("23454234W");

        Mockito.when(dniClient.check(Mockito.any())).thenReturn(ResponseEntity.ok(new CheckDniResponse("OK")));

        mockMvc.perform(post("/sala/1/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testCreateUserWithInvalidName() throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("largonombre");
        request.setEmail("email@email.com");
        request.setPhone("677998899");
        request.setRol("admin");
        request.setDni("23454234W");

        mockMvc.perform(post("/sala/1/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("error validation userName"));
    }

    @Test
    void getUserShouldReturnUserWhenExists() throws Exception {
        Sala sala = new Sala(1L);
        salaRepository.save(sala);

        User user = new User();
        user.setName("pablo");
        user.setEmail("pablo@email.com");
        user.setPhone("677998899");
        user.setRol(Role.ADMIN);
        user.setDni("23454234W");
        user.setSala(sala);

        user = userRepository.save(user);

        mockMvc.perform(get("/sala/1/usuario/" + user.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId())).andExpect(jsonPath("$.name").value("pablo"))
                .andExpect(jsonPath("$.email").value("pablo@email.com"))
                .andExpect(jsonPath("$.phone").value("677998899")).andExpect(jsonPath("$.rol").value(Role.ADMIN))
                .andExpect(jsonPath("$.dni").value("23454234W"));

    }

    @Test
    void getUserShouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(get("/sala/1/usuario/999")).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("Usuario no encontrado en sala 1"));
    }

    @Test
    void shouldReturnConflictWhenDniIsInvalid() throws Exception {
        CreateUserRequest request = new CreateUserRequest("pablo", "email@email.com", "600000000", "admin", "12345678X");

        when(dniClient.check(any())).thenReturn(ResponseEntity.ok(new CheckDniResponse("INVALID")));

        mockMvc.perform(post("/salas/1/usuarios").contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(request)))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.code").value(409))
        .andExpect(jsonPath("$.message").value("error al validar el dni"));

    }

    @Test
    void shouldSaveUserWhenDniIsValid() throws Exception {
        CreateUserRequest request = new CreateUserRequest("pablo", "email@email.com", "600000000", "admin", "12345678X");

        when(dniClient.check(any())).thenReturn(ResponseEntity.ok(new CheckDniResponse("VALID")));

        mockMvc.perform(post("/salas71/usuarios").contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNumber());
    }

}