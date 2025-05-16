package com.peersmarket.marketplace.user.infrastructure.persistence.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.model.AppUserEntity;

public interface AppUserJpaRepository extends JpaRepository<AppUserEntity, Long> {
    Optional<AppUserEntity> findByUsername(String username);
    Optional<AppUserEntity> findByEmail(String email);
    
}
