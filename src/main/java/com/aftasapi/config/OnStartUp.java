package com.aftasapi.config;

import com.aftasapi.entity.Fish;
import com.aftasapi.entity.Level;
import com.aftasapi.repository.FishRepository;
import com.aftasapi.repository.LevelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OnStartUp implements CommandLineRunner {

    private final FishRepository fishRepository;
    //private final LevelRepository levelRepository;

    @Override
    public void run(String... args) {
        //saveFish(levelRepository.findAll());
    }

    public void saveFish(List<Level> levelSet) {
        List<Fish> fishList = new ArrayList<>();
        fishList.add(Fish.builder().name("Bass").averageWeight(3.0D).level(levelSet.get(1)).build());
        fishList.add(Fish.builder().name("Tuna").averageWeight(1.0D).level(levelSet.get(0)).build());
        fishList.add(Fish.builder().name("Cod").averageWeight(8.0D).level(levelSet.get(2)).build());
        fishList.add(Fish.builder().name("Mackerel").averageWeight(5.0D).level(levelSet.get(1)).build());
        fishList.add(Fish.builder().name("Pike").averageWeight(0.5D).level(levelSet.get(0)).build());
        fishList.add(Fish.builder().name("Perch").averageWeight(10.0D).level(levelSet.get(2)).build());
        fishList.add(Fish.builder().name("Catfish").averageWeight(2.0D).level(levelSet.get(1)).build());
        fishList.add(Fish.builder().name("Salmon").averageWeight(10.0D).level(levelSet.get(2)).build());
        fishList.add(Fish.builder().name("Trout").averageWeight(15.0D).level(levelSet.get(3)).build());
        fishList.add(Fish.builder().name("Carp").averageWeight(2.0D).level(levelSet.get(1)).build());
        fishList.add(Fish.builder().name("Swordfish").averageWeight(20.0D).level(levelSet.get(4)).build());
        fishList.add(Fish.builder().name("Marlin").averageWeight(2.0D).level(levelSet.get(2)).build());
        fishList.add(Fish.builder().name("Barracuda").averageWeight(3.0D).level(levelSet.get(1)).build());
        fishList.add(Fish.builder().name("Mahi-Mahi").averageWeight(5.0D).level(levelSet.get(2)).build());
        fishList.add(Fish.builder().name("Bluefish").averageWeight(15.0D).level(levelSet.get(1)).build());
        fishList.add(Fish.builder().name("Walleye").averageWeight(10.0D).level(levelSet.get(1)).build());
        fishList.add(Fish.builder().name("Snapper").averageWeight(200.0D).level(levelSet.get(1)).build());
        fishList.add(Fish.builder().name("Grouper").averageWeight(150.0D).level(levelSet.get(2)).build());
        fishList.add(Fish.builder().name("Haddock").averageWeight(5.0D).level(levelSet.get(1)).build());
        fishList.add(Fish.builder().name("Shark").averageWeight(50.0D).level(levelSet.get(4)).build());
        fishList.add(Fish.builder().name("Eel").averageWeight(2.0D).level(levelSet.get(1)).build());

        fishRepository.saveAll(fishList);
    }


}
