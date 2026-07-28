package com.com.pri_vrat.authentication.service.apikey;

import com.com.pri_vrat.authentication.dto.ApiKeyReqDto;
import com.com.pri_vrat.authentication.dto.AppResponse;

public interface ApiKeyService {
    public AppResponse getApiKeyDetails();

    public void saveApiKey(ApiKeyReqDto apiKeyReqDto);

    public boolean updateApiKeyDetails(ApiKeyReqDto apiKeyReqDto);
}
