package com.com.pri_vrat.authentication.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder(toBuilder = true)
@EqualsAndHashCode
@Entity
@Table(name = "API_KEY_DETAILS")
public class ApiKeyDetails {

    @EmbeddedId
    private ApiPrimaryKey primaryKey;
    private LocalDate creationDate;
    private String apiKey;
    private String status;
}
