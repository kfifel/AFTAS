package com.aftasapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompetitionDTO implements Serializable {

    private String code;
    @NotNull
    private Date date;
    private Time startTime;
    private Time endTime;
    private Integer numberOfParticipant;
    private String location;
    private Double amount;
}
