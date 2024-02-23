package com.aftasapi.web.dto;


import com.aftasapi.entity.enums.IdentityDocumentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberInputDTO implements Serializable {

    @Null(message = "Number must be null")
    private Long number;

    @NotNull(message = "Name is mandatory")
    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotNull(message = "Family name is mandatory")
    @NotBlank(message = "Family name is mandatory")
    private String familyName;

    @NotNull(message = "Birth date is mandatory")
    private Date accessionDate;

    @NotNull(message = "Nationality is mandatory")
    @NotBlank(message = "Nationality could not be blank")
    private String nationality;

    @NotNull(message = "Identity Document Type can not be null")
    private IdentityDocumentType identityDocumentType;

    @NotNull(message = "Identity Number is mandatory")
    private String identityNumber;

    @NotBlank
    private String password;

    @NotBlank
    private String email;
}