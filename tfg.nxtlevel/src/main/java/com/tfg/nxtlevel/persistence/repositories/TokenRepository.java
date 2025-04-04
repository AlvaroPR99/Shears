package com.tfg.nxtlevel.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tfg.nxtlevel.persistence.entities.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {

}
