package com.com.pri_vrat.authentication.service;

import com.com.pri_vrat.authentication.dto.CurrentUserDto;
import com.com.pri_vrat.authentication.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GetCurrentUserService {

    public CurrentUserDto getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication instanceof JwtAuthenticationToken jwtAuthenticationToken)) {
                return new CurrentUserDto();
            }
            Jwt jwt = jwtAuthenticationToken.getToken();
            return CurrentUserDto.builder()
                    .currentTenantId(jwt.getClaimAsString("LOG-TENANT-ID"))
                    .currentUserName(jwt.getClaimAsString("LOG-USER-NAME"))
                    .build();
        } catch (Exception e) {
            log.error(Constants.EXCEPTION.PREFIX, e.getMessage(), "getApiKeyDetails()");
            throw e;
        }
    }

}
