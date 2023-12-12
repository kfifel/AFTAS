package com.aftasapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionRegistrationDTO {

    @NotNull(message = "Member id cannot be null")
    private Long memberId;
}
