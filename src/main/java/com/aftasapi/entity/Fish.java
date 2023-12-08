package com.aftasapi.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Fish {

    @Id
    private String name;
    private Double averageWeight;

    @OneToOne
    private Level level;
}
