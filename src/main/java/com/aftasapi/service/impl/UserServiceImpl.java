package com.aftasapi.service.impl;

import com.aftasapi.entity.AppUser;
import com.aftasapi.entity.Role;
import com.aftasapi.repository.RoleRepository;
import com.aftasapi.repository.UserRepository;
import com.aftasapi.security.AuthoritiesConstants;
import com.aftasapi.security.SecurityUtils;
import com.aftasapi.service.RoleService;
import com.aftasapi.service.UserService;
import com.aftasapi.utils.CustomError;
import com.aftasapi.utils.ValidationException;
import com.aftasapi.web.dto.RoleDto;
import com.aftasapi.web.exception.EmailAlreadyExistException;
import com.aftasapi.web.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.aftasapi.utils.AppConstants.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final RoleRepository roleRepository;

    @Override
    public AppUser save(AppUser user) {
        findByEmail(user.getEmail()).ifPresent(u -> {
            throw new EmailAlreadyExistException();
        });
        if( SecurityUtils.isAuthenticated() &&
            SecurityUtils.hasCurrentUserNoneOfAuthorities(AuthoritiesConstants.ROLE_MANAGER)
        ) {
            user.setAccountNonLocked(true);
        }
        return userRepository.save(user);
    }

    @Override
    public List<AppUser> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<AppUser> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<AppUser> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void revokeRole(Long id, List<RoleDto> roles) throws ValidationException {
        Optional<AppUser> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            AppUser user = userOptional.get();
            List<Role> roleList = new ArrayList<>();
            roles.forEach(roleDto -> roleService.
                    findByName(roleDto.getName()).ifPresent(roleList::add));

            if (new HashSet<>(user.getRoles()).containsAll(roleList)) {
                user.getRoles().removeAll(roleList);
                userRepository.save(user);
            } else {
                throw new ValidationException(CustomError.builder()
                        .field("roles")
                        .message("User does not have all specified roles.")
                        .build());
            }
        }
        else {
            throw new ValidationException(CustomError.builder()
                    .field("user id")
                    .message("User does not exist")
                    .build());
        }
    }

    @Override
    public AppUser assigneRole(Long id, List<RoleDto> roles) throws ValidationException, ResourceNotFoundException {
        Optional<AppUser> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            AppUser user = userOptional.get();
            List<Role> roleList = new ArrayList<>();
            final int[] someExist = {0};
            roles.forEach(roleDto ->
                    roleService.findByName(roleDto.getName())
                            .ifPresentOrElse(
                                    role -> {
                                        if (user.getRoles().contains(role))
                                            someExist[0] = 1;
                                        roleList.add(role);
                                    },
                                    () -> roleList.add(Role.builder().name(roleDto.getName()).build())));
            if(someExist[0] == 1)
                throw new ValidationException(CustomError.builder().field("roles").message("User already has some of specified roles.").build());
            roleRepository.saveAll(roleList);
            user.getRoles().addAll(roleList);
            return userRepository.save(user);
        }
        throw new ResourceNotFoundException(USER_NOT_FOUND);
    }

    @Override
    public List<String> getAuthorities() {
        return roleService.getALlRoles().stream().map(Role::getName).toList();
    }

    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
    }

    @Override
    public AppUser findByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
    }

    @Override
    public List<String> getMyAuthorities() {
        return getCurrentUser()
                .getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }

    @Override
    public AppUser getCurrentUser() {
        String currentUserLogin = SecurityUtils.getCurrentUserEmail();
        if(currentUserLogin == null)
            throw new BadCredentialsException(USER_NOT_FOUND);
        return this.findByUsername(currentUserLogin);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void softDelete(Long id) {
        userRepository.softDelete(id);
    }

    @Override
    public void forceDelete(Long id) {
        userRepository.forceDelete(id);
    }

    @Override
    @Transactional
    public void enable(Long id, boolean enable) {
        if(!userRepository.existsById(id)) {
            throw new UsernameNotFoundException("user not found");
        }
        userRepository.updateEnableById(enable, id);
    }


}
