package com.peersmarket.marketplace.user.infrastructure.persistence.jpa.model;

import java.time.LocalDate;

import com.peersmarket.marketplace.user.domain.model.AppUserRole;
import com.peersmarket.marketplace.user.domain.model.UserStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "app_users")
@Getter
@Setter
@NoArgsConstructor
public class AppUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String avatarUrl;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private LocalDate joinDate;
    
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private AddressEntity address;

    @Column(nullable = false)
    private AppUserRole role;

    private Boolean verified;
}

