package com.com.pri_vrat.authentication.service;

import com.com.pri_vrat.authentication.dto.AppResponse;
import com.com.pri_vrat.authentication.dto.Auth;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface AuthTokenService {
    public AppResponse login(Auth auth) throws Exception;
}
