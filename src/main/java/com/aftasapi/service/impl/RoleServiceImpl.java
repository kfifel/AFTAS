package com.aftasapi.service.impl;

import com.aftasapi.entity.Role;
import com.aftasapi.repository.RoleRepository;
import com.aftasapi.service.RoleService;
import com.aftasapi.utils.CustomError;
import com.aftasapi.utils.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role save(Role role) throws ValidationException {
        Optional<Role> optionalRole = roleRepository.findByName(role.getName());
        if (optionalRole.isPresent())
            throw new ValidationException(new CustomError("name", "Role with this name already exists"));
        return roleRepository.save(role);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public List<Role> getALlRoles()
    {
        return roleRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if(role.isPresent())
            roleRepository.delete(role.get());
        else
            throw new NoSuchElementException("Role not found with id: " + id);
    }

    @Override
    public Role findById(Long id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if(roleOptional.isEmpty())
            throw new IllegalArgumentException("role doesn't exist with this id: " + id);
        return roleOptional.get();
    }
}
