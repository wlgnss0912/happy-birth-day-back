package hbd.cakedecorating.oauth.repository;

import hbd.cakedecorating.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

/**
 *  1.인증(Authentication) - 로그인
 *  2.인가(Authorization) - 권한 부여
 *  OAuth 2.0 인증 요청(클라이언트 ID, 리다이렉션 URL, 요청 권한)을 쿠키에 저장, 검색, 삭제하는 데 사용되는 클래스입니다.
 *  사용자가 구글 로그인 버튼을 클릭할 때마다 OAuth 2.0 인증을 재전송할 필요가 없습니다.
 *
 *  사용자의 인증 요청을 임시로 보관하는 리포지토리에 대한 설정입니다. 이 요청에는 인증 과정을 모두 마친 후 리다이렉트할 프론트의 URI가 담겨있습니다.
 */
@Component
public class CookieAuthorizationRequestRepository implements AuthorizationRequestRepository {

    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    public static final String REFRESH_TOKEN = "refresh_token";
    private static final int COOKIE_EXPIRE_SECONDS = 180;


    // 구글 로그인 버튼 클릭 시, 쿠키에 저장된 OAuth2.0 인증 요청 검색
    // 쿠키에 저장된 OAuth2.0 인증 요청이 존재하면 APP은 인증을 하지 않고 구글 로그인 페이지로 리다이렉션.
    // 2개의 쿠키 유효시간은 180초로 유효시간 내에 인증요청을 다시하면 만들어졌던 쿠키를 다시 사용한다.
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    // OAuth2.0 인증 요청 쿠키에 저장
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if(authorizationRequest == null) {
            CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
            CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
            CookieUtils.deleteCookie(request, response, REFRESH_TOKEN);
            return;
        }

        CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtils.serialize(authorizationRequest), COOKIE_EXPIRE_SECONDS);
        String rediectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);

        if(StringUtils.isNotBlank(rediectUriAfterLogin)) {
            CookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, rediectUriAfterLogin, COOKIE_EXPIRE_SECONDS);
        }

    }

    // 쿠키에서 OAuth2.0 인증 요청 삭제
    // - 로그아웃, 인증 취소 또는 인증 완료 후 실행
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }

    // 인증 요청시 생성된 2개의 쿠키는 인증이 종료될 때
    //      -> successHandler와 failureHandler에서 제거된다.
    // 2개의 쿠키 유효시간은 180초로 유효시간 내에 인증요청을 다시하면 만들어졌던 쿠키를 다시 사용한다.
    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, REFRESH_TOKEN);
    }
}
