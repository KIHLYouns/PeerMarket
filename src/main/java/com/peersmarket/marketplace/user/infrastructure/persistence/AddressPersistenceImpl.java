package com.peersmarket.marketplace.user.infrastructure.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.peersmarket.marketplace.user.application.port.out.AddressRepository;
import com.peersmarket.marketplace.user.domain.model.Address;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.mapper.AddressMapper;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.model.AddressEntity;
import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.repository.AddressJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AddressPersistenceImpl implements AddressRepository {

    private final AddressJpaRepository addressJpaRepository;
    private final AddressMapper addressMapper;

    @Override
    public Address save(final Address address) {

        if (address.getCity() == null || address.getCity().getId() == null) {
            throw new IllegalStateException(
                    "City must be persisted (have an ID) before saving the associated address.");
        }

        final AddressEntity addressEntity = addressMapper.toEntity(address);

        final AddressEntity savedAddressEntity = addressJpaRepository.save(addressEntity);

        return addressMapper.toDomain(savedAddressEntity);
    }

    @Override
    public Optional<Address> findById(final Long id) {

        final Optional<AddressEntity> entityOptional = addressJpaRepository.findById(id);

        return entityOptional.map(addressMapper::toDomain);
    }

}
