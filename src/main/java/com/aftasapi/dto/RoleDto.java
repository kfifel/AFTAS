package com.aftasapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    @NotBlank
    @NotNull
    private String name;
}
