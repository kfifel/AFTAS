package com.aftasapi.dto;

import lombok.*;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LevelDto implements Serializable {
    @NotNull
    @Digits(integer = 2, fraction = 0)
    @Positive
    private Long code;
    private String description;
    @NotNull
    @Positive
    private Integer point;
}