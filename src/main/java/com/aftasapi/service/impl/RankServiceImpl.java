package com.aftasapi.service.impl;

import com.aftasapi.entity.Ranking;
import com.aftasapi.repository.RankingRepository;
import com.aftasapi.service.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RankServiceImpl implements RankService {

    private final RankingRepository rankingRepository;

    @Override
    public Ranking save(Ranking rank) {
        canRankBeSaved(rank);
        return rankingRepository.save(rank);
    }

    private void canRankBeSaved(Ranking rank) {
        if(rankingRepository.existsById(rank.getId())) {
            throw new IllegalArgumentException("Rank already exists");
        }
    }
}
