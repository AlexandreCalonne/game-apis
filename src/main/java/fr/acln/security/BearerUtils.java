package fr.acln.security;

public class BearerUtils {

    public static final String BEARER = "Bearer ";

    public static boolean isRequestBearerAuthentication(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }

    public static String extractTokenFromAuthorizationHeader(String authorizationHeader) {
        System.out.println("EXTRACT: " + authorizationHeader.substring(BEARER.length()).trim());
        return authorizationHeader.substring(BEARER.length()).trim();
    }

}
