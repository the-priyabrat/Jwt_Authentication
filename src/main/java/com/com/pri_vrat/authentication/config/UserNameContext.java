package com.com.pri_vrat.authentication.config;

public class UserNameContext {
    public static ThreadLocal<String> currentUser = new ThreadLocal<>();
}
