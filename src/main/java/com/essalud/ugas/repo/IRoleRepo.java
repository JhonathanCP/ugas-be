package com.essalud.ugas.repo;

import com.essalud.ugas.model.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepo extends IGenericRepo<Role, Integer> {
    Role findByName(String name);
}