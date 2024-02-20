package com.aftasapi.service;

import com.aftasapi.web.dto.FishHuntingDto;
import com.aftasapi.entity.Hunting;
import com.aftasapi.web.exception.ResourceNotFoundException;

public interface HuntingService {

    Hunting save(FishHuntingDto fishHuntingDto, String competitionCode) throws ResourceNotFoundException;

}
