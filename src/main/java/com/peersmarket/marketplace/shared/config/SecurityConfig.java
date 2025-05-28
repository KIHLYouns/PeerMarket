package com.peersmarket.marketplace.shared.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.peersmarket.marketplace.auth.security.JwtAccessDeniedHandler;
import com.peersmarket.marketplace.auth.security.JwtAuthenticationEntryPoint;
import com.peersmarket.marketplace.auth.security.JwtAuthenticationFilter;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(@NotNull final HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // AJOUT de la configuration CORS
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)) // Pour H2 console
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))
                .authorizeHttpRequests(auth -> auth
                        // Endpoints publics (pas besoin d'authentification)
                        .requestMatchers("/api/auth/**").permitAll() // Authentification (login, register)
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll() // Suggestions d'items
                        .requestMatchers(HttpMethod.GET, "/api/items/suggestions").permitAll() // Suggestions d'items
                        .requestMatchers(HttpMethod.GET, "/api/items/{id}").permitAll() // Détail d'un item
                        .requestMatchers(HttpMethod.GET, "/api/items").permitAll() // Liste des items (avec filtres)
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll() // Catégories
                        .requestMatchers(HttpMethod.GET, "/api/cities/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/profile/{id}").permitAll() // Profil public d'un utilisateur
                        .requestMatchers(HttpMethod.GET, "/api/reviews/user/{userId}").permitAll() // Avis sur un utilisateur

                        // Endpoints nécessitant une authentification
                        .requestMatchers("/api/items/**").authenticated() // Créer, modifier, supprimer un item
                        .requestMatchers("/api/users/me/**").authenticated() // Informations de l'utilisateur connecté, mise à jour
                        .requestMatchers("/api/conversations/**").authenticated()
                        .requestMatchers("/api/messages/**").authenticated()
                        .requestMatchers("/api/saved-items/**").authenticated()
                        .requestMatchers("/api/reviews/**").authenticated() // Poster un avis

                        // Endpoints pour administrateurs (si vous en avez)
                        // .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Toute autre requête doit être authentifiée
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // URL de votre frontend Angular
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "X-Requested-With", "Accept", "Origin"));
        configuration.setAllowCredentials(true); // Important si vous utilisez des cookies ou l'authentification HTTP
        configuration.setMaxAge(3600L); // Temps en secondes pendant lequel la réponse pre-flight peut être mise en cache

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Appliquer cette configuration à toutes les routes
        return source;
    }
}