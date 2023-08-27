package com.fronchak.petshopv1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fronchak.petshopv1.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

}
