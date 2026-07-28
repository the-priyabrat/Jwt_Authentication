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
public class ApiPrimaryKey {
    private String tenantId;
    private String email;
    private String userName;
}
