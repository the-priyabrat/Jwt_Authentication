package com.com.pri_vrat.authentication.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder(toBuilder = true)
public class AuthUserPk {
    private String userName;
    private String email;
    private String workspaceId;
}
