package com.aftasapi.web.rest;

import com.aftasapi.service.HuntingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hunting")
@RequiredArgsConstructor
public class HuntingController {

    private final HuntingService huntingService;

    @GetMapping
    public String getHunting() {
        return "Hunting";
    }
}
