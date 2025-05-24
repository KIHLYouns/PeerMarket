package com.peersmarket.marketplace.user.infrastructure.geocoding;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peersmarket.marketplace.user.application.port.out.ReverseGeocoding;
import com.peersmarket.marketplace.user.domain.model.City;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExternalReverseGeocodingApiImpl implements ReverseGeocoding {

    private final RestTemplate restTemplate;
    private final String apiUrl = "https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat={lat}&lon={lon}";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Optional<City> findCityByCoordinates(final Double longitude, final Double latitude) {
        try {
            final String jsonResponse = restTemplate.getForObject(apiUrl, String.class, latitude, longitude);
            if (jsonResponse == null || jsonResponse.isBlank()) {
                log.warn("Received null or blank response from Nominatim for lat={}, lon={}", latitude, longitude);
                return Optional.empty();
            }

            final JsonNode root = objectMapper.readTree(jsonResponse);
            final JsonNode addressNode = root.path("address");

            if (addressNode.isMissingNode()) {
                log.warn("Address node is missing in Nominatim response for lat={}, lon={}. Response: {}", latitude, longitude, jsonResponse);
                return Optional.empty();
            }

            final String cityName = Optional.ofNullable(addressNode.path("city").asText(null))
                                    .filter(name -> !name.isBlank())
                                    .or(() -> Optional.ofNullable(addressNode.path("town").asText(null))
                                            .filter(name -> !name.isBlank()))
                                    .or(() -> Optional.ofNullable(addressNode.path("village").asText(null))
                                            .filter(name -> !name.isBlank()))
                                    .or(() -> Optional.ofNullable(addressNode.path("county").asText(null)) // Ajout de county
                                            .filter(name -> !name.isBlank()))
                                    .or(() -> Optional.ofNullable(addressNode.path("state_district").asText(null)) // Ajout de state_district
                                            .filter(name -> !name.isBlank()))
                                    .or(() -> Optional.ofNullable(addressNode.path("state").asText(null)) // Ajout de state
                                            .filter(name -> !name.isBlank()))
                                    .or(() -> Optional.ofNullable(addressNode.path("country").asText(null)) // Ajout de country comme dernier recours
                                            .filter(name -> !name.isBlank()))
                                    .orElse(null);

            if (cityName != null) {
                final City city = new City(cityName);
                return Optional.of(city);
            } else {
                log.warn("Could not determine city name from Nominatim response for lat={}, lon={}. Address node: {}", latitude, longitude, addressNode.toString());
            }
        } catch (final Exception e) {
            log.error("Error fetching or parsing reverse geocoding data for lat={}, lon={}", latitude, longitude, e);
        }
        return Optional.empty();
    }
}