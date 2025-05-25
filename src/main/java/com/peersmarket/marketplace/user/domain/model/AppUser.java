package com.peersmarket.marketplace.user.domain.model;

import java.time.LocalDate;
import java.util.List;

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
    private Double averageRating;
    private Integer ratingCount;

    public AppUser(final String username, final Email email, final Password password, final AppUserRole role) {
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

    public void changePassword(final Password newPassword) {
        this.password = newPassword;
    }

    public void updateProfile(final String username, final String bio, final String avatarUrl, final Address address) {
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

    /**
     * Recalcule la note moyenne basée sur tous les avis.
     */
    public void recalculateRating(final List<Integer> allRatings) {
        if (allRatings == null || allRatings.isEmpty()) {
            this.ratingCount = 0;
            this.averageRating = 0.0;
            return;
        }

        this.ratingCount = allRatings.size();
        final double sum = allRatings.stream().mapToInt(Integer::intValue).sum();
        this.averageRating = Math.round((sum / this.ratingCount) * 100.0) / 100.0;
    }

    /**
     * Mise à jour incrémentale de la note.
     */
    public void addRatingIncremental(final int newRating) {
        if (this.ratingCount == null) this.ratingCount = 0;
        if (this.averageRating == null) this.averageRating = 0.0;

        final double currentTotalRating = this.averageRating * this.ratingCount;
        this.ratingCount++;
        this.averageRating = Math.round(((currentTotalRating + newRating) / this.ratingCount) * 100.0) / 100.0;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(final Email email) {
        this.email = email;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(final Password password) {
        this.password = password;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(final String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(final String bio) {
        this.bio = bio;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(final LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(final UserStatus status) {
        this.status = status;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(final Address address) {
        this.address = address;
    }

    public AppUserRole getRole() {
        return role;
    }

    public void setRole(final AppUserRole role) {
        this.role = role;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(final boolean verified) {
        this.verified = verified;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(final Double averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(final Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

}