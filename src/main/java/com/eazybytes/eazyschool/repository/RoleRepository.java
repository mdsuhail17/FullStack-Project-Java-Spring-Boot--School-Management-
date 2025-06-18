package com.eazybytes.eazyschool.repository;

import com.eazybytes.eazyschool.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.management.relation.Role;
@Repository
public interface RoleRepository extends JpaRepository<Roles, Integer> {
    Roles getByRoleName(String roleName);
}
