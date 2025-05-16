package com.peersmarket.marketplace.user.domain.model;

import com.peersmarket.marketplace.shared.exception.InvalidAddressException;

public class Address {
    private Long id;
    private City city;
    private Double longitude;
    private Double latitude;

    public void setCoordinates(Double longitude, Double latitude) {
        if (longitude < -180 || longitude > 180 || latitude < -90 || latitude > 90) {
            throw new InvalidAddressException("Invalid coordinates");
        }
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    // Setter ajouté
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
