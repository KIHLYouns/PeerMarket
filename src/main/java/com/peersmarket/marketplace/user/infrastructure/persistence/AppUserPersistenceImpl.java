package com.peersmarket.marketplace.user.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component; // Ou @Repository si vous préférez pour les adaptateurs de persistance

import com.peersmarket.marketplace.user.application.port.out.AppUserRepository;
import com.peersmarket.marketplace.user.domain.model.AppUser;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.mapper.AppUserMapper;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.model.AddressEntity;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.model.AppUserEntity;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.repository.AddressJpaRepository;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.repository.AppUserJpaRepository;

import lombok.RequiredArgsConstructor;

@Component // Ou @Repository
@RequiredArgsConstructor
public class AppUserPersistenceImpl implements AppUserRepository {

    private final AppUserJpaRepository appUserJpaRepository;
    private final AppUserMapper appUserMapper;
    private final AddressJpaRepository addressJpaRepository;

    @Override
    public AppUser save(AppUser appUser) {
        AppUserEntity appUserEntity = appUserMapper.toEntity(appUser);

        if (appUser.getAddress() != null && appUser.getAddress().getId() != null) {
            final AddressEntity AddressEntity = addressJpaRepository.findById(appUser.getAddress().getId())
                    .orElseThrow(() -> new IllegalStateException(
                            "Impossible de trouver l'adresse persistée avec l'ID : " + appUser.getAddress().getId()));
            appUserEntity.setAddress(AddressEntity);
        }
        
        AppUserEntity savedEntity = appUserJpaRepository.save(appUserEntity);
        return appUserMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<AppUser> findById(Long id) {
        return appUserJpaRepository.findById(id)
                .map(appUserMapper::toDomain);
    }

    @Override
    public Optional<AppUser> findByUsername(String username) {
        return appUserJpaRepository.findByUsername(username)
                .map(appUserMapper::toDomain);
    }

    @Override
    public Optional<AppUser> findByEmail(String email) {
        // Assurez-vous que la méthode findByEmail existe dans AppUserJpaRepository
        return appUserJpaRepository.findByEmail(email)
                .map(appUserMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        appUserJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return appUserJpaRepository.existsById(id);
    }
}