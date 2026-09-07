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

import java.util.*;


@Slf4j
@Configuration
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        Map<String, Map<String, Object>> resourceAccess = new HashMap<>();
        Map<String, Object> azpRole = new HashMap<>();
        List<String> roles = new ArrayList<>();
        final String AZP = source.getClaimAsString("azp");
        azpRole = switch (AZP) {
            case "log-ingestion" -> {
                resourceAccess = source.getClaim("resource_access");
                yield resourceAccess.get(AZP);
            }
            case "log-scheduler-service" -> {
                resourceAccess = source.getClaim("resource_access");
                azpRole = resourceAccess.get(AZP);
                yield azpRole;
            }
            default -> azpRole;
        };
        roles = extractRole(azpRole);
        Collection<GrantedAuthority> grantedAuthorities = roles.stream()
                .map(var -> (GrantedAuthority) new SimpleGrantedAuthority(var)).toList();
        return new JwtAuthenticationToken(source, grantedAuthorities);
    }

    private List<String> extractRole(Map<String, Object> azpRole) {
        try {
            List<String> roles = new ArrayList<>();
            if (azpRole == null) {
                roles.add(Constants.KEYCLOAK.DEFAULT_USER);
            } else {
                Object rolesObject = azpRole.get("roles");
                if (rolesObject instanceof List<?> list) {
                    roles = list.stream().map(String::valueOf).toList();
                }
            }
            return roles;
        } catch (Exception e) {
            log.error("Exception at role extraction {}", e.getMessage());
        }
        return Collections.emptyList();
    }
}
