package com.capgemini.test.code.dto;

public class CreateUserResponse {

    private Long id;

    public CreateUserResponse(Long id) {
        this.id = id;
    }
    
    public Long getId() {
        return id;
    }
    
}