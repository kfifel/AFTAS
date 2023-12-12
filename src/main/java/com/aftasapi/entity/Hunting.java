package com.aftasapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Hunting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer numberOfFish;

    @ManyToOne
    @JsonBackReference
    private Fish fish;

    @ManyToOne
    @JsonBackReference
    private Member member;

    @ManyToOne
    @JsonBackReference
    private Competition competition;


}
