package com.learning.scoredemo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.POST, "/api/v1/scorecards/request").hasAuthority("produce:scorecard")
                        .pathMatchers(HttpMethod.GET, "/api/v1/scorecards/latest").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())
                ));
        return http.build();
    }

    Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            log.info("JWT Claims: {}", jwt.getClaims());
            // Read from the "scope" claim for Client Credentials flow
            String scope = jwt.getClaimAsString("scope");
            if (scope == null) {
                log.warn("No 'scope' claim found in JWT");
                return Collections.emptyList();
            }
            Collection<GrantedAuthority> authorities = Arrays.stream(scope.split(" "))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            log.info("Authorities from 'scope' claim: {}", authorities);
            return authorities;
        });
        return new ReactiveJwtAuthenticationConverterAdapter(jwtConverter);
    }
}