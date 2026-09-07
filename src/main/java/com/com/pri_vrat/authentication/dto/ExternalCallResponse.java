package com.com.pri_vrat.authentication.dto;

import lombok.*;
import org.json.simple.JSONObject;

import java.util.List;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
public class ExternalCallResponse<T> {
    private String code;
    private String message;
    private T result;
}
