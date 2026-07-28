package com.com.pri_vrat.authentication.controller;

import com.com.pri_vrat.authentication.dto.*;
import com.com.pri_vrat.authentication.service.AuthTokenService;
import com.com.pri_vrat.authentication.service.AccountManagementService;
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

    private final AccountManagementService userService;
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

    @PostMapping("/logout")
    public ResponseEntity<AppResponse> logOut(@RequestBody AuthRequestDto requestDto) {
        AppResponse response = tokenService.logOut(requestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<AppResponse> refreshToken(@RequestBody AuthRequestDto requestDto) {
        AppResponse response = tokenService.refreshToken(requestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getUserDetails")
    public ResponseEntity<AppResponse> getUserDetails() {
        AppResponse response = userService.getUserDetails();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/apiKeyDetails/{apiKey}")
    public UserApiKeyResponseDto getApiKeyDetails(@PathVariable String apiKey) {
        return userService.getApiKeyDetails(apiKey);
    }
}
