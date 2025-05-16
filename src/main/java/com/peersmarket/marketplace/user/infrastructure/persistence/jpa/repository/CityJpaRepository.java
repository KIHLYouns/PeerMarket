package com.peersmarket.marketplace.user.infrastructure.persistence.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.model.CityEntity;

public interface CityJpaRepository extends JpaRepository<CityEntity, Long> {
    Optional<CityEntity> findByNameIgnoreCase(String name);
}