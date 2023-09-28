package hbd.cakedecorating.oauth.handler;

import hbd.cakedecorating.api.repository.user.UserRefreshTokenRepository;
import hbd.cakedecorating.config.properties.AppProperties;
import hbd.cakedecorating.oauth.repository.CookieAuthorizationRequestRepository;
import hbd.cakedecorating.oauth.token.JwtTokenProvider;
import hbd.cakedecorating.utils.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static hbd.cakedecorating.oauth.repository.CookieAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private String redirectUri = "http://localhost:3000/redirect";
    private final JwtTokenProvider jwtTokenProvider;
    private final AppProperties appProperties;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if(response.isCommitted()) {
            log.debug("Response has already been committed.");
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        //cookie uri가 존재 + 허용된 도메인인가
        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new IllegalArgumentException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());// 이는 사용자가 인증에 실패하면 /login 페이지로 리디렉션, error라는 쿼리 파라미터가 추가

        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;

        //JWT 생성
        //UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        return UriComponentsBuilder.fromUriString(targetUrl)
                //.queryParam("token", tokenInfo.getAccessToken())
                .build().toUriString();

    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);
        URI authorizedUri = URI.create(redirectUri);//수정필요

        // 포트 + 도메인이 허용된 것인지 확인
        if(authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())// getHost() - 도메인 부분 추출
                && authorizedUri.getPort() == clientRedirectUri.getPort()) {
            return true;
        }
        return false;
    }

}
