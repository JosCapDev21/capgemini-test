package com.capgemini.test.code.repository;

import java.util.Optional;

import com.capgemini.test.code.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByIdAndSalaId(Long id, Long salaId);

    Optional<User> findByEmail(String email);

}