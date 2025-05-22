package com.capgemini.test.code.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

    // @NotBlank
    // @Size(max = 6)
    private String name;

    // @NotBlank
    // @Email
    private String email;

    // @NotBlank
    private String phone;

    // @Pattern(regexp = "admin|superadmin")
    private String rol;

    // @NotBlank
    private String dni;
}