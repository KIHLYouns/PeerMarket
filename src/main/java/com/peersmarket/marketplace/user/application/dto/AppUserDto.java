package com.peersmarket.marketplace.user.application.dto;

import java.time.LocalDate;

import com.peersmarket.marketplace.user.domain.model.AppUserRole;
import com.peersmarket.marketplace.user.domain.model.UserStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppUserDto {
    private Long id;
    private String username;
    private String email;
    private String avatarUrl;
    private String bio;
    private LocalDate joinDate;
    private UserStatus status;
    private AppUserRole role;
    private Boolean verified;
    private AddressDto address;
}
