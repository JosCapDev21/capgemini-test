package com.capgemini.test.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.test.code.entity.Sala;

public interface SalaRepository extends JpaRepository<Sala, Long> {

}