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
    private List<Image> images;
    private Integer viewCount;

    public Item() {
        this.images = new ArrayList<>();
        this.viewCount = 0;
        this.status = ItemStatus.AVAILABLE;
        this.createdAt = LocalDateTime.now();
    }

    private Item(final ItemBuilder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
        this.price = builder.price;
        this.condition = builder.condition;
        this.status = builder.status;
        this.seller = builder.seller;
        this.category = builder.category;
        this.createdAt = builder.createdAt;
        this.images = builder.images;
        this.viewCount = builder.viewCount;
    }

    public static ItemBuilder builder() {
        return new ItemBuilder();
    }

    public static class ItemBuilder {
        private Long id;
        private String title;
        private String description;
        private BigDecimal price;
        private ItemCondition condition;
        private ItemStatus status;
        private AppUser seller;
        private Category category;
        private LocalDateTime createdAt;
        private List<Image> images;
        private Integer viewCount;

        public ItemBuilder() {
        }

        public ItemBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        public ItemBuilder title(final String title) {
            this.title = title;
            return this;
        }

        public ItemBuilder description(final String description) {
            this.description = description;
            return this;
        }

        public ItemBuilder price(final BigDecimal price) {
            this.price = price;
            return this;
        }

        public ItemBuilder condition(final ItemCondition condition) {
            this.condition = condition;
            return this;
        }

        public ItemBuilder status(final ItemStatus status) {
            this.status = status;
            return this;
        }

        public ItemBuilder seller(final AppUser seller) {
            this.seller = seller;
            return this;
        }

        public ItemBuilder category(final Category category) {
            this.category = category;
            return this;
        }

        public ItemBuilder createdAt(final LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ItemBuilder images(final List<Image> images) {
            if (images != null) {
                this.images = new ArrayList<>(images);
            } else {
                this.images = new ArrayList<>();
            }
            return this;
        }

        public ItemBuilder viewCount(final Integer viewCount) {
            this.viewCount = viewCount;
            return this;
        }

        public Item build() {
            final Item item = new Item(this);
            return item;
        }
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

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(final Integer viewCount) {
        this.viewCount = viewCount;
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
