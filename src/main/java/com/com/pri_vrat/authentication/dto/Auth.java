package com.com.pri_vrat.authentication.dto;

import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class Auth {
    private String email;
    private String password;
}
