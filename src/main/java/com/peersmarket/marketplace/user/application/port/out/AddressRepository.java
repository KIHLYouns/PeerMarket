package com.peersmarket.marketplace.user.application.port.out;

import java.util.Optional;

import com.peersmarket.marketplace.user.domain.model.Address;

public interface AddressRepository {
    
    //List<Address> findAll();
    Optional<Address> findById(Long id); 
    Address save(Address address);
    //void deleteById(Long id); 
}
