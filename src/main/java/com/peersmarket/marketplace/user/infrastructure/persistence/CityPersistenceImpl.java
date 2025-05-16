package com.peersmarket.marketplace.user.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.peersmarket.marketplace.user.application.port.out.CityRepository;
import com.peersmarket.marketplace.user.domain.model.City;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.mapper.CityMapper;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.model.CityEntity;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.repository.CityJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CityPersistenceImpl implements CityRepository{

    private final CityJpaRepository cityJpaRepository;
    private final CityMapper cityMapper;
    
    @Override
    public City save(final City city) {
        final CityEntity cityEntity = cityMapper.toEntity(city);
        return cityMapper.toDomain(cityJpaRepository.save(cityEntity));
    }

    @Override
    public Optional<City> findByNameIgnoreCase(final String name) {
        return cityJpaRepository.findByNameIgnoreCase(name)
                .map(cityMapper::toDomain);
    }
    
}
