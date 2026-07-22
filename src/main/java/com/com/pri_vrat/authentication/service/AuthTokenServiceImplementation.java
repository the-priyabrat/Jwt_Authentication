package com.com.pri_vrat.authentication.service;

import com.com.pri_vrat.authentication.config.RoleContext;
import com.com.pri_vrat.authentication.config.TenantContext;
import com.com.pri_vrat.authentication.dto.AppResponse;
import com.com.pri_vrat.authentication.dto.Auth;
import com.com.pri_vrat.authentication.dto.LogoutRequestDto;
import com.com.pri_vrat.authentication.entity.UserAuth;
import com.com.pri_vrat.authentication.exception.customException.AuthenticationException;
import com.com.pri_vrat.authentication.repository.AuthUserRepository;
import com.com.pri_vrat.authentication.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthTokenServiceImplementation implements AuthTokenService {

    private final RestTemplate restTemplate;
    private final MessageSource messageSource;
    private final AuthUserRepository authRepo;

    @Value("${docmanager.keycloak.token.endpoint}")
    private String tokenEndPoint;

    @Value("${docmanager.keycloak.client.secret}")
    private String clientSecret;

    @Value("${docmanager.keycloak.client.id}")
    private String clientId;

    @Value("${docmanager.keycloak.realm}")
    private String realm;

    @Value("${log.keycloak.logout.endpoint}")
    private String serverUri;

    @Override
    public AppResponse login(Auth auth) throws Exception {
        AppResponse loginResponse = new AppResponse();
        try {
            List<UserAuth> userListByEmail = authRepo.findByUserPrimaryKey_Email(auth.getEmail());
            if (userListByEmail.isEmpty()) {
                throw new AuthenticationException(messageSource.getMessage("MESSAGE.AUTH.INVALID.EMAIL", null, Locale.ENGLISH));
            } else if (!userListByEmail.getFirst().getUserPrimaryKey().getEmail().equals(auth.getEmail())) {
                throw new AuthenticationException(messageSource.getMessage("MESSAGE.AUTH.INVALID.EMAIL", null, Locale.ENGLISH));
            }
            AppResponse response = getToken(auth);
            if (!response.getCode().equals(Constants.RESPONSE_CODE.SUCCESS)) {
                throw new AuthenticationException(messageSource.getMessage("MESSAGE.AUTH.INVALID.AUTH", null, Locale.ENGLISH));
            }
            JSONObject tokenResponse = response.getDetails().getFirst();
            String token = tokenResponse.get("access_token").toString();
            JSONObject parsedJwt = parseAuthToken(token);
            if (parsedJwt != null) {
                RoleContext.currentRole.set(parsedJwt.get(Constants.KEYCLOAK.LOG_USER_TYPE).toString());
                TenantContext.currentTenant.set(parsedJwt.get(Constants.KEYCLOAK.LOG_TENANT_ID).toString());
            }
            loginResponse.setCode(Constants.RESPONSE_CODE.SUCCESS);
            loginResponse.setMessage(messageSource.getMessage("MESSAGE.AUTH.LOGIN.SUCCESS", null, Locale.ENGLISH));
            loginResponse.setDetails(List.of(tokenResponse));
            return loginResponse;
        } catch (AuthenticationException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.info("Exception occurred in auth token service at login...{}", e.getMessage());
            throw e;
        }
    }

    @Override
    public AppResponse logOut(LogoutRequestDto requestDto) {
        log.info("Entering in log out method");
        if (requestDto != null && requestDto.getRefreshToken() == null) {
            throw new AuthenticationException("Insufficient data");
        }
        try {
            HttpEntity<MultiValueMap<String, String>> requestBody = getHttpEntityForRefreshToken(requestDto.getRefreshToken());
            ResponseEntity<JSONObject> response = restTemplate.postForEntity(serverUri , requestBody, JSONObject.class);
            AppResponse appResponse = new AppResponse();
            if(response.getStatusCode().toString().equals("204 NO_CONTENT")) {
                appResponse =AppResponse.builder()
                        .code("SUCCESS")
                        .message(messageSource.getMessage("USER.LOGGED.OUT.SUCCESS", null, Locale.ENGLISH))
                        .details(List.of())
                        .build();
            }
            return appResponse;
        } catch (Exception e) {
            log.error("exception at logOut() method...{}", e.getMessage());
            throw e;
        }
    }

    private HttpEntity<MultiValueMap<String, String>> getHttpEntityForRefreshToken(String refreshToken) {
        log.info("Entering getHttpEntityForRefreshToken()");
        try{
            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("client_secret", clientSecret);
            requestBody.add("realm", realm);
            requestBody.add("refresh_token", refreshToken);
            requestBody.add("client_id", clientId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            return new HttpEntity<>(requestBody, headers);
        } catch(Exception e) {
            log.error("exception at getHttpEntityForRefreshToken() method...{}", e.getMessage());
            throw e;
        }
    }

    private AppResponse getToken(Auth auth) {
        log.info("Entering into token service getToken()...");
        AppResponse appResponse = new AppResponse();
        try {
            HttpEntity<?> requiredEntity = getHttpEntity(auth);
            JSONObject response = restTemplate.exchange(tokenEndPoint, HttpMethod.POST, requiredEntity, JSONObject.class).getBody();
            appResponse.setCode(Constants.RESPONSE_CODE.SUCCESS);
            appResponse.setMessage("Successfully fetched");
            assert response != null;
            appResponse.setDetails(List.of(response));
            return appResponse;
        } catch (HttpClientErrorException | AuthenticationException ex) {
            log.info(ex.getMessage());
            throw ex;
        } catch (Exception e) {
            log.error("exception at getToken() method...{}", e.getMessage());
            throw e;
        }
    }

    private HttpEntity<?> getHttpEntity(Auth auth) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", clientId);
        requestBody.add("realm", realm);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("grant_type", Constants.KEYCLOAK.GRANT_TYPE);
        requestBody.add("username", auth.getEmail());
        requestBody.add("password", auth.getPassword());
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>(requestBody, header);
    }

    private JSONObject parseAuthToken(String authToken) throws Exception {
        try {
            assert authToken != null;
            String[] tokenPartitions = authToken.split("\\.");
            if (tokenPartitions.length != 3) {
                throw new AuthenticationException(messageSource.getMessage("MESSAGE.AUTH.INVALID.TOKEN", null, Locale.ENGLISH));
            }
            Base64.Decoder decoder = Base64.getUrlDecoder();
            String jwtHeader = new String(decoder.decode(tokenPartitions[0].toString()));
            String jwtBody = new String(decoder.decode(tokenPartitions[1].toString()));

            ObjectMapper mapper = new ObjectMapper();
            JSONObject parsedHeader = mapper.readValue(jwtHeader, JSONObject.class);
            JSONObject parseBody = mapper.readValue(jwtBody, JSONObject.class);
            parseBody.put("header", parsedHeader);
            return parseBody;
        } catch (Exception e) {
            log.info("Exception occurred in token parser..{}", e.getMessage());
            throw e;
        }
    }
}
