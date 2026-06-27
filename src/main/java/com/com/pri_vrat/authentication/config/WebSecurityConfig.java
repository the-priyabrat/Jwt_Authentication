package com.com.pri_vrat.authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    JwtAuthConverter jwtAuthConverter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((req) ->
                req.requestMatchers("/authuser/api/register").permitAll()
                        .requestMatchers("/authuser/api/login").permitAll()
                        .requestMatchers("/authuser/api/verify").permitAll()
                        .anyRequest().authenticated()
        );
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);
        http.oauth2ResourceServer((auth) ->
                auth.jwt((jwt) -> jwt.jwtAuthenticationConverter(new JwtAuthConverter()))
        );
        return http.build();
    }

}
