package com.aftasapi.config;

import com.aftasapi.entity.AppUser;
import com.aftasapi.entity.Fish;
import com.aftasapi.entity.Level;
import com.aftasapi.entity.Role;
import com.aftasapi.repository.FishRepository;
import com.aftasapi.repository.LevelRepository;
import com.aftasapi.repository.RoleRepository;
import com.aftasapi.repository.UserRepository;
import com.aftasapi.security.AuthoritiesConstants;
import com.aftasapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OnStartUp implements CommandLineRunner {

    private final FishRepository fishRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final LevelRepository levelRepository;
    private final RoleRepository roleRepository;
    private static final String PASSWORD = "password";
    @Override
    public void run(String... args) {
        List<Role> roles = createRoles();
        createUsers(roles);
        if (userRepository.count() > 0) {
            try {
                userService.save(AppUser.builder()
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("password"))
                        .firstName("khalid")
                        .lastName("fifel")
                        .verifiedAt(LocalDateTime.now())
                        .enabled(true)
                        .credentialsNonExpired(true)
                        .accountNonLocked(true)
                        .accountNonExpired(true)
                        .build());
            }catch (Exception e) {

            }
        }
        if(levelRepository.count() == 0) {
            levelRepository.saveAll(this.getLevels());
            saveFish(levelRepository.findAll());
        }

    }

    private List<Level> getLevels() {

        return List.of(
                Level.builder().code(1L).point(500).build(),
                Level.builder().code(2L).point(1000).build(),
                Level.builder().code(3L).point(1500).build(),
                Level.builder().code(4L).point(2000).build(),
                Level.builder().code(5L).point(2500).build()
        );
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
    private void createUsers(List<Role> roles) {
        if(userRepository.count() > 0)
            return;

        AppUser manager = AppUser.builder()
                .email("admin@gmail.com")
                .password(passwordEncoder.encode(PASSWORD))
                .firstName("Admin")
                .lastName("User")
                .roles(roles)
                .verifiedAt(LocalDateTime.now())
                .build();
        userRepository.save(manager);

        roles.forEach(role -> {

            if (role.getName().equals(AuthoritiesConstants.ROLE_JUDGE))
                userRepository.save(AppUser.builder()
                        .email("judge@gmail.com")
                        .password(passwordEncoder.encode(PASSWORD))
                        .firstName("judge")
                        .lastName("User")
                        .verifiedAt(LocalDateTime.now())
                        .roles(List.of(role))
                        .build());
            if (role.getName().equals(AuthoritiesConstants.ROLE_MEMBER)) {
                userRepository.save(AppUser.builder()
                        .email("member@gmail.com")
                        .password(passwordEncoder.encode(PASSWORD))
                        .firstName("Tenant")
                        .lastName("User")
                        .verifiedAt(LocalDateTime.now())
                        .roles(List.of(role))
                        .build());
            }
        });
    }

    private List<Role> createRoles() {
        if(roleRepository.count() > 0)
            return roleRepository.findAll();

        return roleRepository.saveAll(List.of(
                Role.builder().name(AuthoritiesConstants.ROLE_MANAGER).build(),
                Role.builder().name(AuthoritiesConstants.ROLE_JUDGE).build(),
                Role.builder().name(AuthoritiesConstants.ROLE_MEMBER).build()
        ));
    }

}
