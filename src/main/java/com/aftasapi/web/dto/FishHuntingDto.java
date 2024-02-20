package com.aftasapi.web.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FishHuntingDto implements Serializable {

    @NotNull(message = "Fish weight is required")
    @Positive(message = "Fish weight must be positive")
    private Double fishWeight;

    @NotNull(message = "Fish name is required")
    private String fishName;

    @NotNull(message = "Member id is required")
    private Long memberId;
    private String competitionCode;
}
