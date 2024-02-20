package com.aftasapi.web.dto;

import lombok.*;

import java.io.Serializable;



@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FishDto implements Serializable {

    String name;
    Double averageWeight;
    LevelDto level;
}