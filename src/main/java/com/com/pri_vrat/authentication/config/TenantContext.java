package com.com.pri_vrat.authentication.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TenantContext {
    public static ThreadLocal<String> currentTenant = new ThreadLocal<>();
}
