package com.com.pri_vrat.authentication.dto;

public interface RequiredRequestAuthProjection {
    String getEmail();
    String getUserName();
    String getTenantId();
    String getApiKey();
    String getPassword();
    String getUserType();
}
