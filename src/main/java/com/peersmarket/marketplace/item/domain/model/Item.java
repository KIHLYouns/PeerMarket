package com.peersmarket.marketplace.item.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.peersmarket.marketplace.user.domain.model.AppUser;

public class Item {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private ItemCondition condition;
    private ItemStatus status;
    private AppUser seller;
    private Category category;
    private LocalDateTime createdAt;

    public Item() {
        this.createdAt = LocalDateTime.now();
    }

    public Item(final String title, final String description, final BigDecimal price, final ItemCondition condition, final AppUser seller, final Category category) {
        this();
        this.title = title;
        this.description = description;
        this.price = price;
        this.condition = condition;
        this.seller = seller;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ItemCondition getCondition() {
        return condition;
    }

    public void setCondition(ItemCondition condition) {
        this.condition = condition;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public AppUser getSeller() {
        return seller;
    }

    public void setSeller(AppUser seller) {
        this.seller = seller;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    
    
}
