package com.omar.utils;

public class AuthTokenHolder {
    private static String token;

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        AuthTokenHolder.token = token;
    }
}
