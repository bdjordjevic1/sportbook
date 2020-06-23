package com.ronin.sportbook.common.security.payload;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "{email.notBlank}")
    @Email(message = "{email.invalid.format}")
    private String email;

    @NotBlank(message = "{password.notBlank}")
    private String password;
}
