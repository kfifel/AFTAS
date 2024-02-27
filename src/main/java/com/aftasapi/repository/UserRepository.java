package com.aftasapi.repository;

import com.aftasapi.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);

    @Modifying
    @Query("UPDATE AppUser u SET u.enabled = :enable WHERE u.id = :id")
    void updateEnableById(boolean enable, Long id);

    @Query("SELECT u FROM AppUser u WHERE u.id = ?1 AND u.deletedAt IS NULL")
    void softDelete(Long id);

    @Query("SELECT u FROM AppUser u WHERE u.id = ?1 AND u.deletedAt IS NOT NULL")
    void forceDelete(Long id);
}
