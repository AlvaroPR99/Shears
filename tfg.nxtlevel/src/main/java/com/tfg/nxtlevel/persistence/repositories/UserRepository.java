package com.tfg.nxtlevel.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tfg.nxtlevel.persistence.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
