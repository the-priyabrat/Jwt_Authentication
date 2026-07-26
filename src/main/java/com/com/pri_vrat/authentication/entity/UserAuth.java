package com.com.pri_vrat.authentication.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.View;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "LOG_USER_AUTH")
@Entity
public class UserAuth {
    @EmbeddedId
    private AuthUserPk userPrimaryKey;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String applicationName;
    private String status;
    private String password;
    private String tenantId;
    private String userType;
}
