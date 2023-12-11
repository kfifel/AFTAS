package com.aftasapi.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FishHuntingDto {

    private Double fishWeight;
    private String fishName;
    private Long memberId;
    private String competitionCode;
}
