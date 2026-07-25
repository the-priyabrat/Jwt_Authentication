package com.com.pri_vrat.authentication.config;

import com.com.pri_vrat.authentication.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


@Slf4j
@Configuration
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        Map<String, Map<String, Object>> resourceAccess = source.getClaim("resource_access");
        Map<String, Object> logIngestion = resourceAccess.get("log-ingestion");
        List<String> roles = new ArrayList<>();
        if (logIngestion == null) {
            roles.add(Constants.KEYCLOAK.DEFAULT_USER);
        } else {
            Object rolesObject = logIngestion.get("roles");

            if (rolesObject instanceof List<?> list) {
                roles = list.stream()
                        .map(String::valueOf)
                        .toList();
            }
        }
        Collection<GrantedAuthority> grantedAuthorities = roles.stream()
                .map(var -> (GrantedAuthority) new SimpleGrantedAuthority(var)).toList();
        return new JwtAuthenticationToken(source, grantedAuthorities);
    }
}
