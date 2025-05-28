package com.peersmarket.marketplace.user.infrastructure.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.peersmarket.marketplace.user.application.dto.CityDto;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.mapper.CityMapper;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.repository.CityJpaRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {
    private final CityJpaRepository cityJpaRepository;
    private final CityMapper cityMapper;

    @GetMapping
    public List<CityDto> getCities() {
        return cityJpaRepository.findAllByOrderByNameAsc().stream()
                .map(cityMapper::toDomain)
                .map(cityMapper::toDto)
                .toList();
    }
}