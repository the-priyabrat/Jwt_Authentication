package com.com.pri_vrat.authentication.controller;

import com.com.pri_vrat.authentication.dto.AppResponse;
import com.com.pri_vrat.authentication.dto.ClientAuthRequest;
import com.com.pri_vrat.authentication.service.AuthTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/internal/authenticate")
public class InternalServiceVerifierController {

    private final AuthTokenService authTokenService;

    @PostMapping("/scheduler")
    public ResponseEntity<AppResponse> authenticateClient(@RequestBody ClientAuthRequest clientAuthRequest) {
        return new ResponseEntity<>(authTokenService.clientLogin(clientAuthRequest), HttpStatus.OK);
    }
}
