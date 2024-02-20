package com.aftasapi.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionRegistrationDTO implements Serializable {

    @NotNull(message = "Member id cannot be null")
    private Long memberId;
}
