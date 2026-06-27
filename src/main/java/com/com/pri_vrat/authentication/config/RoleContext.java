package com.com.pri_vrat.authentication.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoleContext {
    public static ThreadLocal<String> currentRole = new ThreadLocal<>();
}
