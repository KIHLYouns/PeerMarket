package com.peersmarket.marketplace.item.domain.model;

public class Category {
    private Long id;
    private String name;

    public Category() {}

    public Category(final String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
