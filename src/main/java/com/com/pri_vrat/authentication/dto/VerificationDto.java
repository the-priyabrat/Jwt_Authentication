package com.com.pri_vrat.authentication.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class VerificationDto {
    private String email;
}
