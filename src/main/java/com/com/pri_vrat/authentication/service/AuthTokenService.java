package com.com.pri_vrat.authentication.service;

import com.com.pri_vrat.authentication.dto.AppResponse;
import com.com.pri_vrat.authentication.dto.Auth;
import com.com.pri_vrat.authentication.dto.AuthRequestDto;
import com.com.pri_vrat.authentication.dto.ClientAuthRequest;

public interface AuthTokenService {
    public AppResponse login(Auth auth) throws Exception;

    public AppResponse clientLogin(ClientAuthRequest clientAuthRequest);

    public AppResponse logOut(AuthRequestDto requestDto);

    public AppResponse refreshToken(AuthRequestDto requestDto);
}
