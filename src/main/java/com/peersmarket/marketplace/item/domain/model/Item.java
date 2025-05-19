package com.peersmarket.marketplace.item.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private List<Image> images = new ArrayList<>();

    public Item() {
        this.createdAt = LocalDateTime.now();
        this.images = new ArrayList<>();
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

    public void setId(final Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public ItemCondition getCondition() {
        return condition;
    }

    public void setCondition(final ItemCondition condition) {
        this.condition = condition;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(final ItemStatus status) {
        this.status = status;
    }

    public AppUser getSeller() {
        return seller;
    }

    public void setSeller(final AppUser seller) {
        this.seller = seller;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(final Category category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(final List<Image> images) {
        this.images = images;
    }

    public void addImage(final Image image) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        this.images.add(image);
        image.setItemId(this.getId());
    }

    public void removeImage(final Image image) {
        if (this.images != null) {
            this.images.remove(image);
        }
        image.setItemId(null);
    }
}
