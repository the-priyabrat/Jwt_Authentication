package com.com.pri_vrat.authentication.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class CurrentUserDto {
    private String currentUserName;
    private String currentTenantId;
}
