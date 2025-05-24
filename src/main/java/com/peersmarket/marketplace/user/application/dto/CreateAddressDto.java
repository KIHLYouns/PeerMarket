package com.peersmarket.marketplace.user.application.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAddressDto {

    @NotNull(message = "La longitude ne peut pas être nulle.")
    @DecimalMin(value = "-180.0", message = "La longitude doit être comprise entre -180.0 et 180.0.")
    @DecimalMax(value = "180.0", message = "La longitude doit être comprise entre -180.0 et 180.0.")
    private Double longitude;

    @NotNull(message = "La latitude ne peut pas être nulle.")
    @DecimalMin(value = "-90.0", message = "La latitude doit être comprise entre -90.0 et 90.0.")
    @DecimalMax(value = "90.0", message = "La latitude doit être comprise entre -90.0 et 90.0.")
    private Double latitude;
}
