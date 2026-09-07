package com.com.pri_vrat.authentication.service.external;

import com.com.pri_vrat.authentication.dto.AppResponse;
import com.com.pri_vrat.authentication.dto.ExternalCallResponse;
import com.com.pri_vrat.authentication.dto.RequiredRequestAuthProjection;

import java.util.List;

public interface ExternalFeignRespondService {
    public ExternalCallResponse<List<RequiredRequestAuthProjection>> getActiveAuthUserDetailsWithKey();
}
