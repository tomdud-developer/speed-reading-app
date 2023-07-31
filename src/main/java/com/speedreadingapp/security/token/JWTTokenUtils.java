package com.speedreadingapp.security.token;

public class JWTTokenUtils {

    private JWTTokenUtils() {}

    public static boolean isContainAuthorizationHeaderWithBearerKeyword(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }

    public static String retrieveTokenFromHeader(String authorizationHeader) {
        return authorizationHeader.substring("Bearer ".length());
    }
}