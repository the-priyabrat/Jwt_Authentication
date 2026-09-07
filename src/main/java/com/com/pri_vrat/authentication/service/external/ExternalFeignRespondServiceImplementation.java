package com.com.pri_vrat.authentication.service.external;

import com.com.pri_vrat.authentication.dto.AppResponse;
import com.com.pri_vrat.authentication.dto.ExternalCallResponse;
import com.com.pri_vrat.authentication.dto.RequiredRequestAuthProjection;
import com.com.pri_vrat.authentication.exception.customException.NoDataFoundException;
import com.com.pri_vrat.authentication.repository.AuthUserRepository;
import com.com.pri_vrat.authentication.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ExternalFeignRespondServiceImplementation implements ExternalFeignRespondService{

    private final AuthUserRepository authUserRepository;

    @Override
    public ExternalCallResponse<List<RequiredRequestAuthProjection>> getActiveAuthUserDetailsWithKey() {
        try {
            log.info("Entered into getActiveAuthUserDetailsWithKey()");
            List<RequiredRequestAuthProjection> requiredAuthDetails = authUserRepository.getRequiredAuthDetailsForFeignRespond();
            if(requiredAuthDetails.isEmpty()) {
                throw new NoDataFoundException("No active user found for current data");
            }
            ExternalCallResponse<List<RequiredRequestAuthProjection>> response = new ExternalCallResponse<>();
            return response.toBuilder()
                    .code("SUCCESS")
                    .message("Fetched successfully !")
                    .result(requiredAuthDetails)
                    .build();
        } catch (Exception e) {
            log.error("Exception at getActiveAuthUserDetailsWithKey() failed for {}",e.getMessage());
            throw e;
        }
    }
}
