package com.peersmarket.marketplace.review.application.event;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class ReviewCreatedEvent extends ApplicationEvent {

    private final Long reviewId;
    private final Long revieweeId;
    private final int rating;
    private final LocalDateTime createdAt;

    /**
     * Crée un nouvel événement ReviewCreatedEvent.
     *
     * @param source      L'objet sur lequel l'événement s'est produit initialement (ou l'objet qui publie).
     * @param reviewId    L'ID de l'avis créé.
     * @param revieweeId  L'ID de l'utilisateur évalué.
     * @param rating      La note donnée.
     * @param createdAt   Le moment de la création.
     */
    public ReviewCreatedEvent(final Object source, final Long reviewId, final Long revieweeId, final int rating, final LocalDateTime createdAt) {
        super(source);
        this.reviewId = reviewId;
        this.revieweeId = revieweeId;
        this.rating = rating;
        this.createdAt = createdAt;
    }
}
