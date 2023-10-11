package hbd.cakedecorating.api.controller.auth;

import hbd.cakedecorating.common.ApiResponse;
import hbd.cakedecorating.api.model.user.UserRefreshToken;
import hbd.cakedecorating.api.repository.user.UserRefreshTokenRepository;
import hbd.cakedecorating.config.properties.AppProperties;
import hbd.cakedecorating.oauth.model.Role;
import hbd.cakedecorating.oauth.token.AuthToken;
import hbd.cakedecorating.oauth.token.AuthTokenProvider;
import hbd.cakedecorating.utils.CookieUtils;
import hbd.cakedecorating.utils.HeaderUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    private final static long THREE_DAYS_MSEC = 259200000;
    private final static String REFRESH_TOKEN = "refresh_token";

    @GetMapping("/refresh")
    public ApiResponse refreshToken (HttpServletRequest request, HttpServletResponse response) {
        // accessToken 확인
        String accessToken = HeaderUtils.getAccessToken(request);
        AuthToken authToken = tokenProvider.convertAuthToken(accessToken);

        //이게 굳이 필요한지?
//        if(!authToken.validate()) { // 유효 시 pass
//            return ApiResponse.invalidAccessToken();
//        }

        // expired access token 확인
        Claims claims = authToken.getExpiredTokenClaims();
        if(claims == null) { // 만료되지 않았으면 pass
            return ApiResponse.notExpiredTokenYet();
        }

        String userId = claims.getSubject();
        Role role = Role.of(claims.get("role", String.class));

        // refresh Token - cookie 확인
        //-----------------------------------------------------------------------------
        String refreshToken = CookieUtils.getCookie(request, REFRESH_TOKEN)
                .map(Cookie::getValue)
                .orElse(null);
        AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);

        if(!authRefreshToken.validate()) { // 유효하면 pass
            return ApiResponse.invalidRefreshToken();
        }

        // userId refresh Token - DB 확인
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserIdAndRefreshToken(userId, refreshToken);
        if(userRefreshToken == null) {
            return ApiResponse.invalidRefreshToken();
        }

        Date now = new Date();
        AuthToken newAccessToken = tokenProvider.createAuthToken(
                userId,
                role.getKey(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        long validTime = authRefreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();

        // refresh Token의 기간이 3일 이하 -> 갱신
        if(validTime <= THREE_DAYS_MSEC) {
            // refresh Token 설정
            long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

            authRefreshToken = tokenProvider.createAuthToken(
                    appProperties.getAuth().getTokenSecret(),
                    new Date(now.getTime() + refreshTokenExpiry)
            );

            // refresh Token - DB update
            userRefreshToken.setRefreshToken(authRefreshToken.getToken());

            int cookieMaxAge = (int) refreshTokenExpiry / 60;
            CookieUtils.deleteCookie(request, response, REFRESH_TOKEN);
            CookieUtils.addCookie(response, REFRESH_TOKEN, authRefreshToken.getToken(), cookieMaxAge);
        }
        return ApiResponse.success("token", newAccessToken.getToken());
    }
}
