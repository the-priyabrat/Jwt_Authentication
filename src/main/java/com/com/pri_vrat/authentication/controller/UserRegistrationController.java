package com.com.pri_vrat.authentication.controller;

import com.com.pri_vrat.authentication.dto.AppResponse;
import com.com.pri_vrat.authentication.dto.Auth;
import com.com.pri_vrat.authentication.dto.AuthUserDto;
import com.com.pri_vrat.authentication.dto.VerificationDto;
import com.com.pri_vrat.authentication.service.AuthTokenService;
import com.com.pri_vrat.authentication.service.UserAuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("userAuth/api")
@CrossOrigin("http://localhost:4300")
public class UserRegistrationController {

    private final UserAuthService userService;
    private final AuthTokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<AppResponse> saveUser(@Valid @RequestBody AuthUserDto authDto) throws Exception {
        AppResponse response = userService.registerUser(authDto);
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/verify")
    public ResponseEntity<AppResponse> verifyUser(@RequestBody VerificationDto verificationDto) {
        AppResponse response = userService.verifyUser(verificationDto);
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AppResponse> login(@RequestBody Auth auth) throws Exception {
        AppResponse response = tokenService.login(auth);
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }
}
