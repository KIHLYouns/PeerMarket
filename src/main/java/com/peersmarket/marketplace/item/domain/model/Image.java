package com.peersmarket.marketplace.item.domain.model;

public class Image {
    private Long id;
    private String url;
    private Long itemId;

    public Image() {
    }

    public Image(final String url, final Long itemId) {
        this.url = url;
        this.itemId = itemId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(final Long itemId) {
        this.itemId = itemId;
    }
}
