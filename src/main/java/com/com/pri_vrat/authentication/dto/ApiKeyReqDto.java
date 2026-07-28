package com.com.pri_vrat.authentication.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder(toBuilder = true)
public class ApiKeyReqDto {
    private String apiKey;
    private String email;
    private String userName;
    private String tenantId;
    private String newApiKey;
}
