package com.aftasapi.dto.request;

import com.aftasapi.security.validation.UniqueUsername;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @UniqueUsername
    private String email;

    @NotBlank(message = "Password cannot be blank")
    //@Min(value = 6,message = "Password should be at least 6 characters")
    private String password;

    private List<String> authorities;
}
