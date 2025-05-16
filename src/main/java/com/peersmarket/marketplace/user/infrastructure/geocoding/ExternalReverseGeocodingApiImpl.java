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
                return Optional.empty();
            }

            final JsonNode root = objectMapper.readTree(jsonResponse);
            final JsonNode addressNode = root.path("address");

            final String cityName = Optional.ofNullable(addressNode.path("city").asText(null))
                                    .filter(name -> !name.isBlank())
                                    .or(() -> Optional.ofNullable(addressNode.path("town").asText(null))
                                            .filter(name -> !name.isBlank()))
                                    .or(() -> Optional.ofNullable(addressNode.path("village").asText(null))
                                            .filter(name -> !name.isBlank()))
                                    .orElse(null);

            if (cityName != null) {
                final City city = new City();
                city.setName(cityName);
                return Optional.of(city);
            }
        } catch (final Exception e) {
            log.error("Error fetching reverse geocoding data", e);
        }
        return Optional.empty();
    }
}