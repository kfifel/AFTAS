package com.aftasapi.repository;

import com.aftasapi.entity.Ranking;
import com.aftasapi.entity.embedded.RankingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, RankingId> {
}