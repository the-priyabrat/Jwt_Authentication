package com.com.pri_vrat.authentication.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder(toBuilder = true)
public class ClientAuthRequest {
    private String clientId;
    private String clientSecret;
}
