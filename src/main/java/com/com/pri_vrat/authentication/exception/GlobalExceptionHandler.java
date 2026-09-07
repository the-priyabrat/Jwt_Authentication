package com.com.pri_vrat.authentication.exception;

import com.com.pri_vrat.authentication.dto.AppResponse;
import com.com.pri_vrat.authentication.exception.customException.*;
import com.com.pri_vrat.authentication.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;

@SuppressWarnings("unchecked")
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppResponse> ExceptionHandler(Exception e) {
        log.error("Unexpected exception occurred with message ",e);
        return new ResponseEntity<>(
                AppResponse.builder()
                        .code("FAILED")
                        .message("Something went wrong !")
                        .details(Collections.EMPTY_LIST)
                        .build(), HttpStatus.OK
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AppResponse> validationException(MethodArgumentNotValidException ex) {
        log.info("Method validation exception");
        AppResponse exceptionResponse = new AppResponse();
        exceptionResponse.setCode(Constants.RESPONSE_CODE.FAILED);
        exceptionResponse.setMessage(
                messageSource.getMessage("MESSAGE.REGISTRATION.ARGUMENTS.INVALID", null, Locale.ENGLISH));
        JSONObject exceptionHolder = new JSONObject();
        BindingResult exception = ex.getBindingResult();
        for (FieldError allError : exception.getFieldErrors()) {
            if (!exceptionHolder.containsKey(allError.getObjectName())) {
                exceptionHolder.put(allError.getField(), allError.getDefaultMessage());
            }
        }
        exceptionResponse.setDetails(List.of(exceptionHolder));
        return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), HttpStatus.OK);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<AppResponse> authenticationException(AuthenticationException ex) {
        log.info("Authentication failed, exception occurred....");
        AppResponse exceptionResponse = new AppResponse();
        exceptionResponse.setCode(Constants.RESPONSE_CODE.FAILED);
        exceptionResponse.setMessage(ex.getMessage());
        exceptionResponse.setDetails(Collections.emptyList());
        return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), HttpStatus.OK);
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<AppResponse> registrationException(RegistrationException ex) {
        log.info("Registration failed, exception occurred....");
        AppResponse exceptionResponse = new AppResponse();
        exceptionResponse.setCode(Constants.RESPONSE_CODE.FAILED);
        exceptionResponse.setMessage(ex.getMessage());
        exceptionResponse.setDetails(Collections.emptyList());
        return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), HttpStatus.OK);
    }

    @ExceptionHandler(VerificationException.class)
    public ResponseEntity<AppResponse> verificationException(VerificationException ex) {
        log.info("Verification failed, exception occurred....");
        AppResponse exceptionResponse = new AppResponse();
        exceptionResponse.setCode(Constants.RESPONSE_CODE.FAILED);
        exceptionResponse.setMessage(ex.getMessage());
        exceptionResponse.setDetails(Collections.emptyList());
        return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), HttpStatus.OK);
    }

    @ExceptionHandler(ApiKeyGenerationException.class)
    public ResponseEntity<AppResponse> handelApiKeyException(ApiKeyGenerationException ex) {
        log.info("Key generation failed, exception occurred....");
        AppResponse exceptionResponse = new AppResponse();
        exceptionResponse.setCode(Constants.RESPONSE_CODE.FAILED);
        exceptionResponse.setMessage(ex.getMessage());
        exceptionResponse.setDetails(Collections.emptyList());
        return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), HttpStatus.OK);
    }

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<AppResponse> handelNoDataFoundException(NoDataFoundException ex) {
        log.info("No data found exception occurred....");
        AppResponse exceptionResponse = new AppResponse();
        exceptionResponse.setCode(Constants.RESPONSE_CODE.FAILED);
        exceptionResponse.setMessage(ex.getMessage());
        exceptionResponse.setDetails(Collections.emptyList());
        return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), HttpStatus.OK);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<AppResponse> verificationException(HttpClientErrorException ex) {
        log.info("Httpclient error exception occurred....");
        log.info(ex.getMessage());
        AppResponse exceptionResponse = new AppResponse();
        exceptionResponse.setCode(Constants.RESPONSE_CODE.FAILED);
        JSONObject errorBody = ex.getResponseBodyAs(JSONObject.class);
        Object errorDescription = errorBody != null ? errorBody.get("error_description") : null;
        if (errorDescription != null && Constants.KEYCLOAK.ACCOUNT_DISABLED.equalsIgnoreCase(errorDescription.toString())) {
            exceptionResponse.setMessage(messageSource.getMessage("MESSAGE.ACCOUNT.NOT.ACTIVATED", null, Locale.ENGLISH));
        } else {
            exceptionResponse.setMessage(messageSource.getMessage("MESSAGE.EXCEPTION.OCCURRED", null, Locale.ENGLISH));
        }
        exceptionResponse.setDetails(Collections.emptyList());
        return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), HttpStatus.OK);
    }

}
