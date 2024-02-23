package com.aftasapi.repository;

import com.aftasapi.entity.SequenceGenerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SequenceGeneratorRepository extends JpaRepository<SequenceGenerator, Long> {

    @Query(value = "SELECT sg.nextId FROM SequenceGenerator sg WHERE sg.entity = :entity")
    Optional<Long> findNextValue(@Param("entity") String entityName);

    @Query(value = "UPDATE SequenceGenerator sg SET sg.nextId = :nextValue WHERE sg.id = :id")
    void updateNextValue(
            @Param("nextValue") Long id,
            @Param("id") Long nextValue);
}
