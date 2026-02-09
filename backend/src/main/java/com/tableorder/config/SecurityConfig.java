package com.tableorder.config;

import com.tableorder.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration @EnableWebSecurity @RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(c -> c.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(a -> a
                .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/api/stores/*/sse/orders").hasRole("ADMIN")
                .requestMatchers("/api/stores/*/tables/*/sse/orders").hasRole("TABLE")
                .requestMatchers("/api/stores/*/tables/*/orders/**").hasAnyRole("TABLE", "ADMIN")
                .requestMatchers("/api/orders/*/status", "/api/orders/*").hasRole("ADMIN")
                .requestMatchers("/api/stores/*/tables/**").hasRole("ADMIN")
                .requestMatchers("/api/stores/*/menus/order").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/stores/*/menus/**", "/api/stores/*/categories").hasAnyRole("TABLE", "ADMIN")
                .requestMatchers("/api/stores/*/menus/**").hasRole("ADMIN")
                .requestMatchers("/api/stores/*/orders").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}
