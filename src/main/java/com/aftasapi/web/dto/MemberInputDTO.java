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

    private Long number;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Family name is mandatory")
    private String familyName;

    @NotNull(message = "Birth date is mandatory")
    private Date accessionDate;

    @NotBlank(message = "Nationality could not be blank")
    private String nationality;

    @NotNull(message = "Identity Document Type can not be null")
    private IdentityDocumentType identityDocumentType;

    @NotNull(message = "Identity Number is mandatory")
    private String identityNumber;
}