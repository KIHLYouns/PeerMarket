package com.peersmarket.marketplace.auth.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.peersmarket.marketplace.auth.dto.AuthRequest;
import com.peersmarket.marketplace.auth.dto.AuthResponse;
import com.peersmarket.marketplace.auth.security.JwtUtils;
import com.peersmarket.marketplace.user.application.dto.AppUserDto;
import com.peersmarket.marketplace.user.application.dto.CreateUserDto;
import com.peersmarket.marketplace.user.application.port.in.AppUserService;
import com.peersmarket.marketplace.user.application.port.out.AppUserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AppUserRepository appUserRepository;
    private final AppUserService appUserService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody final AuthRequest authRequest) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        final Long userId = appUserRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();

        final String accessToken = jwtUtils.generateAccessToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(accessToken, userId));
    }

    @PostMapping("/register") 
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody final CreateUserDto createUserDto) {
        
        final AppUserDto createdUser = appUserService.createUser(createUserDto);
        
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(createUserDto.getUsername(), createUserDto.getPassword())
        );
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final String accessToken = jwtUtils.generateAccessToken(userDetails);

        
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(accessToken, createdUser.getId()));
    }
}
