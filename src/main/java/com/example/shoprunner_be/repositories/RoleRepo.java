package com.example.shoprunner_be.repositories;

import com.example.shoprunner_be.entitys.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepo extends CrudRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
