package com.learning.scoredemo.controller;

import com.learning.scoredemo.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtEncoder encoder;
    private final ReactiveAuthenticationManager authenticationManager;

    @PostMapping("/token")
    public Mono<ResponseEntity<String>> token(@RequestBody LoginRequest loginRequest) {
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

        return authenticationManager.authenticate(authenticationToken)
                .map(authentication -> {
                    Instant now = Instant.now();
                    long expiry = 36000L; // 10 hours

                    String scope = authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(" "));

                    JwtClaimsSet claims = JwtClaimsSet.builder()
                            .issuer("self")
                            .issuedAt(now)
                            .expiresAt(now.plusSeconds(expiry))
                            .subject(authentication.getName())
                            .claim("scope", scope)
                            .build();

                    String token = this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
                    return ResponseEntity.ok(token);
                });
    }
}