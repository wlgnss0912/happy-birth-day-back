package hbd.cakedecorating.utils;

import jakarta.servlet.http.HttpServletRequest;

public class HeaderUtils {

    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    public static String getAccessToken(HttpServletRequest request) {
        String headerValue = request.getHeader(HEADER_AUTHORIZATION);//authorization 값 return

        if(headerValue == null) {
            return null;
        }

        if(headerValue.startsWith(TOKEN_PREFIX)) {
            return headerValue.substring(TOKEN_PREFIX.length());//token 값 반환
        }

        return null;
    }

}
