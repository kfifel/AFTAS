package com.aftasapi.service;

import com.aftasapi.entity.AppUser;
import com.aftasapi.utils.ValidationException;
import com.aftasapi.web.dto.RoleDto;
import com.aftasapi.web.exception.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    AppUser save (AppUser user);

    List<AppUser> findAll();

    Optional<AppUser> findById(Long id);

    Optional<AppUser> findByEmail(String email);

    void revokeRole(Long id, List<RoleDto> roles) throws ValidationException;

    AppUser assigneRole(Long id, List<RoleDto> roles) throws ValidationException, ResourceNotFoundException;

    List<String> getAuthorities();

    UserDetailsService userDetailsService();

    AppUser findByUsername(String username);

    List<String> getMyAuthorities();

    public AppUser getCurrentUser();

    void delete(Long id);

    void softDelete(Long id);

    void forceDelete(Long id);
}
