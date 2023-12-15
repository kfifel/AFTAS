package com.aftasapi.service;

import com.aftasapi.entity.Ranking;

import java.util.List;

public interface RankService {
    Ranking save(Ranking rank);

    void saveAll(List<Ranking> rankings);
}
