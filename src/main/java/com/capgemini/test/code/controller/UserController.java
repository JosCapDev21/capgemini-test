package com.capgemini.test.code.controller;

import java.util.Map;

import com.capgemini.test.code.dto.CreateUserRequest;
import com.capgemini.test.code.dto.CreateUserResponse;
import com.capgemini.test.code.entity.User;
import com.capgemini.test.code.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sala/{salaId}/usuario")
public class UserController {
    @Autowired private UserService userService;
    
    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@PathVariable Long salaId, @RequestBody CreateUserRequest req) {
        CreateUserResponse response = userService.createUser(salaId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping
    public ResponseEntity<Map<String, Long>> create(@Valid @RequestBody CreateUserRequest request) {
        Long id = userService.saveUser(request, 1L);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", id));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long salaId, @PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(salaId, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long salaId) {
        User user = userService.getUserById(salaId, 1L);

        return ResponseEntity.ok(user);
    }
}
    