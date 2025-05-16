package com.peersmarket.marketplace.user.application.port.out;

import java.util.Optional;
import com.peersmarket.marketplace.user.domain.model.AppUser;

public interface AppUserRepository {
    AppUser save(AppUser appUser);
    Optional<AppUser> findById(Long id);
    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);
    void deleteById(Long id);
    boolean existsById(Long id);
}
