package com.capgemini.test.code.service;

import com.capgemini.test.code.clients.CheckDniRequest;
import com.capgemini.test.code.clients.CheckDniResponse;
import com.capgemini.test.code.clients.DniClient;
import com.capgemini.test.code.dto.CreateUserRequest;
import com.capgemini.test.code.dto.CreateUserResponse;
import com.capgemini.test.code.entity.Role;
import com.capgemini.test.code.entity.Sala;
import com.capgemini.test.code.entity.User;
import com.capgemini.test.code.exceptions.ConflictException;
import com.capgemini.test.code.exceptions.NotFoundException;
import com.capgemini.test.code.exceptions.UserAlreadyExistsException;
import com.capgemini.test.code.exceptions.ValidationException;
import com.capgemini.test.code.repository.SalaRepository;
import com.capgemini.test.code.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    @Autowired private UserRepository userRepository;
    @Autowired private SalaRepository salaRepository;
    @Autowired private DniClient dniClient;
    @Autowired private NotificationService notificationService;
    
    public Long saveUser(CreateUserRequest request, Long salaId) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("error al validar el mail");
        }

        CheckDniRequest checkRequest = new CheckDniRequest(request.getDni());

        ResponseEntity<CheckDniResponse> dniResponse = dniClient.check(checkRequest);

        if (!"VALID".equalsIgnoreCase(dniResponse.getBody().getMessage())) {
            throw new ValidationException("error al validar el dni");
        }

        Sala sala = salaRepository.findById(salaId).orElseThrow(() -> new NotFoundException("Sala no encontrada"));

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRol(Role.valueOf(request.getRol().toUpperCase()));
        user.setDni(request.getDni());
        user.setSala(sala);

        User savedUser = userRepository.save(user);

        notificationService.notifyUser(savedUser);

        return savedUser.getId();
    }

    public CreateUserResponse createUser(Long salaId, CreateUserRequest request) {
        validateInput(request);
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("error validation email");
        }
    
        ResponseEntity<CheckDniResponse> response = dniClient.check(new CheckDniRequest(request.getDni()));
        if (!"OK".equalsIgnoreCase(response.getBody().getMessage())) {
            throw new ConflictException("error validation dni");
        }
    
        Sala sala = salaRepository.findById(salaId).orElseThrow(() -> new NotFoundException("Sala no encontrada"));
    
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRol(Role.valueOf(request.getRol().toUpperCase()));
        user.setDni(request.getDni());
        user.setSala(sala);
        userRepository.save(user);
    
        notificationService.notify(
            user.getRol() == Role.ADMIN ? user.getEmail() : user.getPhone(),
            user.getRol()
        );
    
        return new CreateUserResponse(user.getId());
    }
    
    public User getUserById(Long salaId, Long userId) {
        return userRepository.findByIdAndSalaId(userId, salaId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }
    
    public User getUserByIdAndSala(Long userId, Long salaId) {
        return userRepository.findByIdAndSalaId(userId, salaId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado en sala " + salaId));
    }

    private void validateInput(CreateUserRequest req) {
        if (req.getName() == null || req.getName().length() > 6) {
            throw new ConflictException("error validation userName");
        }
        if (!req.getEmail().contains("@") || !req.getEmail().contains(".")) {
            throw new ConflictException("error validation email");
        }
        try {
            Role.valueOf(req.getRol().toUpperCase());
        } catch (Exception e) {
            throw new ConflictException("error validation rol");
        }
    }
}
    