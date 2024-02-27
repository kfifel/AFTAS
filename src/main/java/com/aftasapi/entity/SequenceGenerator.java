package com.aftasapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
/**
 * This class is used to generate sequence number for the entities
 * you can use this class to generate sequence number for any entity
 * explain:
 * | entity is the name of the entity you want to generate sequence number for
 * | nextId is the next sequence number to be generated
*/
public class SequenceGenerator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String entity;

    private Long nextId;
}
