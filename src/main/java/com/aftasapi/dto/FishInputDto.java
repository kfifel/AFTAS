package com.aftasapi.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FishInputDto implements Serializable {
    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    String name;
    @NotNull(message = "Average weight cannot be null")
    @Positive(message = "Average weight must be positive")
    Double averageWeight;
    @NotNull(message = "Level code cannot be null")
    @Positive(message = "Level code must be positive")
    Long levelCode;
}