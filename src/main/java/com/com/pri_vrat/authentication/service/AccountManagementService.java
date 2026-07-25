package com.com.pri_vrat.authentication.service;

import com.com.pri_vrat.authentication.dto.AppResponse;
import com.com.pri_vrat.authentication.dto.AuthUserDto;
import com.com.pri_vrat.authentication.dto.VerificationDto;
import com.com.pri_vrat.authentication.exception.customException.RegistrationException;

public interface AccountManagementService {
    public AppResponse registerUser(AuthUserDto authUserDto) throws RegistrationException;

    public AppResponse verifyUser(VerificationDto verificationDto);

    public AppResponse getUserDetails();
}
