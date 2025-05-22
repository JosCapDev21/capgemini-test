package com.capgemini.test.code.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.capgemini.test.code.clients.DniClient;
import com.capgemini.test.code.dto.CreateUserRequest;
import com.capgemini.test.code.entity.Sala;
import com.capgemini.test.code.entity.User;
import com.capgemini.test.code.exceptions.ConflictException;
import com.capgemini.test.code.exceptions.NotFoundException;
import com.capgemini.test.code.repository.SalaRepository;
import com.capgemini.test.code.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SalaRepository salaRepository;

    @Mock
    private DniClient dniClient;

    @Mock
    private NotificationService notificationService;

    @Test
    void testEmailAlreadyExistsThrowsConflict() {
        CreateUserRequest req = new CreateUserRequest();
        req.setName("pablo");
        req.setEmail("test@test.com");
        req.setPhone("000");
        req.setRol("admin");
        req.setDni("X0000000T");

        Mockito.when(userRepository.existsByEmail("test@test.com")).thenReturn(true);

        ConflictException ex = assertThrows(ConflictException.class, () -> userService.createUser(1L, req));
        assertEquals("error validation email", ex.getMessage());
    }

    @Test
    void getUserByIdAndSala_shouldReturnUser_whenExists() {
        Long userId = 1L;
        Long salaId = 1L;
        User user = new User();
        user.setId(userId);
        user.setSala(new Sala(salaId));

        when(userRepository.findByIdAndSalaId(userId, salaId)).thenReturn(Optional.of(user));

        User result = userService.getUserByIdAndSala(userId, salaId);

        assertNotNull(result);
        assertEquals(userId, result.getId());

        verify(userRepository).findByIdAndSalaId(userId, salaId);
    }

    @Test
    void getUserByIdAndSala_shouldThrow_whenUserNotFound() {
        Long userId = 1L;
        Long salaId = 1L;

        when(userRepository.findByIdAndSalaId(userId, salaId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.getUserByIdAndSala(userId, salaId));

        assertEquals("Usuario no encontrado en sala 1", exception.getMessage());

    }

}
