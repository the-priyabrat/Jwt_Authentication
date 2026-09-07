package com.com.pri_vrat.authentication.controller;

import com.com.pri_vrat.authentication.dto.AppResponse;
import com.com.pri_vrat.authentication.dto.ExternalCallResponse;
import com.com.pri_vrat.authentication.dto.RequiredRequestAuthProjection;
import com.com.pri_vrat.authentication.service.AccountManagementService;
import com.com.pri_vrat.authentication.service.apikey.ApiKeyService;
import com.com.pri_vrat.authentication.service.external.ExternalFeignRespondService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/apiKey")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;
    private final AccountManagementService accountManagementService;
    private final ExternalFeignRespondService externalFeignRespondService;

    @GetMapping("/getDetails")
    public ResponseEntity<AppResponse> getApiKeyDetails() {
        AppResponse response = apiKeyService.getApiKeyDetails();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/regenerate")
    public ResponseEntity<AppResponse> regenerateApiResponse() {
        AppResponse response = accountManagementService.regenerateAccessToken();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getDetails/apikies")
    public ResponseEntity<ExternalCallResponse<List<RequiredRequestAuthProjection>>> getAuthUserDetailsWithApiKey(){
        return new ResponseEntity<>(externalFeignRespondService.getActiveAuthUserDetailsWithKey(), HttpStatus.OK);
    }
}
