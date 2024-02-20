package com.aftasapi.web.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LevelDto implements Serializable {

    private Long code;
    private String description;

    @NotNull
    @Positive
    private Integer point;
}