package com.com.pri_vrat.authentication.service.apikey;

import com.com.pri_vrat.authentication.dto.ApiKeyReqDto;
import com.com.pri_vrat.authentication.dto.AppResponse;
import com.com.pri_vrat.authentication.dto.CurrentUserDto;
import com.com.pri_vrat.authentication.entity.ApiKeyDetails;
import com.com.pri_vrat.authentication.entity.ApiPrimaryKey;
import com.com.pri_vrat.authentication.exception.customException.ApiKeyGenerationException;
import com.com.pri_vrat.authentication.repository.ApiKeyRepository;
import com.com.pri_vrat.authentication.service.GetCurrentUserService;
import com.com.pri_vrat.authentication.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class ApiKeyServiceImplementation implements ApiKeyService {


    private final ApiKeyRepository apiKeyRepository;
    private final GetCurrentUserService currentUserService;
    private final MessageSource messageSource;


    @Override
    public AppResponse getApiKeyDetails() {
        try {
            CurrentUserDto currentUserDto = currentUserService.getCurrentUser();
            String tenantId = currentUserDto.getCurrentTenantId();
            String userName = currentUserDto.getCurrentUserName();
            List<ApiKeyDetails> apiKeyDetailsList =
                    apiKeyRepository.findByPrimaryKey_UserNameAndPrimaryKey_TenantId(userName, tenantId);
            if (apiKeyDetailsList.isEmpty()) {
                throw new ApiKeyGenerationException("No key found for this user");
            }
            ApiKeyDetails apiKeyDetails = apiKeyDetailsList.getFirst();
            JSONObject response = new JSONObject();
            response.put("userName", apiKeyDetails.getPrimaryKey().getUserName());
            response.put("email", apiKeyDetails.getPrimaryKey().getEmail());
            response.put("apiKey", apiKeyDetails.getApiKey());
            response.put("createdAt", apiKeyDetails.getCreationDate());
            response.put("status", apiKeyDetails.getStatus());
            return AppResponse.builder()
                    .code("SUCCESS")
                    .message(messageSource.getMessage("API.KEY.FETCHED.SUCCESSFULLY", null, Locale.ENGLISH))
                    .details(List.of(response))
                    .build();
        } catch (Exception e) {
            log.error(Constants.EXCEPTION.PREFIX, e.getMessage(), "getApiKeyDetails()");
            throw e;
        }
    }

    @Async
    @Override
    public void saveApiKey(ApiKeyReqDto apiKeyReqDto) {
        log.info("Entering into saveApiKey details");
        List<ApiKeyDetails> apiKeyDetailList = apiKeyRepository.findByApiKey(apiKeyReqDto.getApiKey());
        if (!apiKeyDetailList.isEmpty()) {
            throw new ApiKeyGenerationException("Duplicate key generation failed");
        }
        try {
            ApiPrimaryKey apiPrimaryKey = ApiPrimaryKey.builder()
                    .userName(apiKeyReqDto.getUserName())
                    .email(apiKeyReqDto.getEmail())
                    .tenantId(apiKeyReqDto.getTenantId())
                    .build();
            ApiKeyDetails apiKeyDetails = ApiKeyDetails.builder()
                    .creationDate(LocalDate.now())
                    .primaryKey(apiPrimaryKey)
                    .apiKey(apiKeyReqDto.getApiKey())
                    .status(Constants.API_KEY.ACTIVE)
                    .build();
            apiKeyRepository.save(apiKeyDetails);
            log.info("Api key details saved successfully");
        } catch (Exception e) {
            log.error(Constants.EXCEPTION.PREFIX, e.getMessage(), "saveApiKey(ApiKeyReqDto apiKeyReqDto)");
            throw e;
        }
    }

    @Override
    public boolean updateApiKeyDetails(ApiKeyReqDto apiKeyReqDto) {
        CurrentUserDto currentUser = currentUserService.getCurrentUser();
        if (currentUser == null) {
            throw new ApiKeyGenerationException(messageSource.getMessage("API.KEY.GENERATION.FAILED", null, Locale.ENGLISH));
        }
        ApiPrimaryKey primaryKey = ApiPrimaryKey.builder()
                .tenantId(currentUser.getCurrentTenantId())
                .email(apiKeyReqDto.getEmail())
                .userName(currentUser.getCurrentUserName())
                .build();
        Optional<ApiKeyDetails> apiKeyDetailsOptional = apiKeyRepository.findById(primaryKey);
        if (apiKeyDetailsOptional.isEmpty()) {
            throw new ApiKeyGenerationException(messageSource.getMessage("API.KEY.GENERATION.FAILED.NO.OLDER.ID", null, Locale.ENGLISH));
        }
        try {
            ApiKeyDetails apiKeyDetails = apiKeyDetailsOptional.get();
            apiKeyDetails.setApiKey(apiKeyReqDto.getNewApiKey());
            apiKeyDetails.setPrimaryKey(primaryKey);
            apiKeyDetails.setCreationDate(LocalDate.now());
            apiKeyRepository.save(apiKeyDetails);
            return true;
        } catch (Exception e) {
            log.error(Constants.EXCEPTION.PREFIX, e.getMessage(), "saveApiKey(ApiKeyReqDto apiKeyReqDto)");
            return false;
        }
    }


}
