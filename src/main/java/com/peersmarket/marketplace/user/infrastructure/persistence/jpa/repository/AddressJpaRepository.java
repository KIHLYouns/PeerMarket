package com.peersmarket.marketplace.user.infrastructure.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.model.AddressEntity;

public interface AddressJpaRepository extends JpaRepository<AddressEntity, Long> {
}