package com.tableorder.config;

import com.tableorder.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
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
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                .antMatchers("/api/auth/**", "/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .antMatchers("/api/stores/*/sse/orders").hasRole("ADMIN")
                .antMatchers("/api/stores/*/tables/*/sse/orders").hasRole("TABLE")
                .antMatchers("/api/stores/*/tables/*/orders/**").hasAnyRole("TABLE", "ADMIN")
                .antMatchers("/api/orders/*/status", "/api/orders/*").hasRole("ADMIN")
                .antMatchers("/api/stores/*/tables/**").hasRole("ADMIN")
                .antMatchers("/api/stores/*/menus/order").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/stores/*/menus/**", "/api/stores/*/categories").hasAnyRole("TABLE", "ADMIN")
                .antMatchers("/api/stores/*/menus/**").hasRole("ADMIN")
                .antMatchers("/api/stores/*/orders").hasRole("ADMIN")
                .anyRequest().authenticated()
            .and()
            .headers().frameOptions().disable()
            .and()
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}
