package com.aftasapi.repository;

import com.aftasapi.entity.SequenceGenerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SequenceGeneratorRepository extends JpaRepository<SequenceGenerator, Long> {

    @Query(value = "SELECT sg.nextId FROM SequenceGenerator sg WHERE sg.entity = :entity")
    Optional<Long> findNextValue(@Param("entity") String entityName);

    @Modifying
    @Query("""
        UPDATE SequenceGenerator s SET s.nextId = :nextId WHERE s.entity = :entity
    """)
    void updateNextIdByEntity(
           @Param("entity") String entity,
           @Param("nextId") Long nextId);
}
