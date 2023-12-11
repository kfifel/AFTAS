package com.aftasapi.service;

import com.aftasapi.dto.FishHuntingDto;
import com.aftasapi.entity.Hunting;
import com.aftasapi.exception.ResourceNotFoundException;

public interface HuntingService {

    Hunting save(FishHuntingDto fishHuntingDto) throws ResourceNotFoundException;

}
