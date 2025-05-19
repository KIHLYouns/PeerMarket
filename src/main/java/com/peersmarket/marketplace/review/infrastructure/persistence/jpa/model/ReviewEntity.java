package com.peersmarket.marketplace.review.infrastructure.persistence.jpa.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.peersmarket.marketplace.user.infrastructure.persistence.jpa.model.AppUserEntity; // Assurez-vous que le chemin est correct

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "review") // Nom de la table comme dans database.erd
@Getter
@Setter
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int rating;

    @Column(columnDefinition = "TEXT", nullable = true) // Le commentaire peut être optionnel ou non
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private AppUserEntity reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewee_id", nullable = false)
    private AppUserEntity reviewee;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
