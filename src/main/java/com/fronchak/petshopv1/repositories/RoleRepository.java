package com.fronchak.petshopv1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fronchak.petshopv1.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
