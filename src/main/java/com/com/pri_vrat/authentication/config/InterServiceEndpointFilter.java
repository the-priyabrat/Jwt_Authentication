package com.com.pri_vrat.authentication.config;

import com.com.pri_vrat.authentication.util.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Configuration
public class InterServiceEndpointFilter extends OncePerRequestFilter {

    @Value("${auth.public.uri.access.key}")
    private String publicApiKey;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null) {
            filterChain.doFilter(request, response);
            return;
        }
        String requestURI = request.getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean matches =
                antPathMatcher
                        .match("/authentication-service/userAuth/api/apiKeyDetails/**", requestURI);
        //the next public endpoints to be added by || like || antMatcher.matches(example/get/**)
        if (matches) {
            final String requestAccessKey = request.getHeader(Constants.CustomHeader.X_ACCESS_KEY);
            if (!Objects.equals(publicApiKey, requestAccessKey)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
