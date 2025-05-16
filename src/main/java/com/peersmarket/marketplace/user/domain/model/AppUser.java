package com.peersmarket.marketplace.user.domain.model;

import java.time.LocalDate;

import com.peersmarket.marketplace.shared.model.Email;
import com.peersmarket.marketplace.shared.model.Password;

public class AppUser {

    private Long id;
    private String username;
    private Email email;
    private Password password;
    private String avatarUrl;
    private String bio;
    private LocalDate joinDate;
    private UserStatus status;
    private Address address;
    private AppUserRole role;
    private Boolean verified;

    public AppUser(String username, Email email, Password password, AppUserRole role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.joinDate = LocalDate.now();
        this.status = UserStatus.ACTIVE;
        this.verified = false;
    }

    public void verifyAccount() {
        if (this.status == UserStatus.ACTIVE) {
            this.verified = true;
        }
    }

    public void changePassword(Password newPassword) {
        this.password = newPassword;
    }

    public void updateProfile(String username, String bio, String avatarUrl, Address address) {
        if (username != null && !username.trim().isEmpty()) {
            this.username = username;
        }
        if (bio != null) {
            this.bio = bio;
        }
        if (avatarUrl != null) {
            this.avatarUrl = avatarUrl;
        }
        if (address != null) {
            this.address = address;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public AppUserRole getRole() {
        return role;
    }

    public void setRole(AppUserRole role) {
        this.role = role;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

}