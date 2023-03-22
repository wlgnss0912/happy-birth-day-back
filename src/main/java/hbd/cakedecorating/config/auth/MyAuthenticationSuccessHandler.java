package hbd.cakedecorating.config.auth;

import hbd.cakedecorating.model.user.SocialType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        log.info("success!");

        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        SocialType socialType = SocialType.valueOf(authToken.getAuthorizedClientRegistrationId());
        log.info("socialType={}", socialType);

        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();

        String[] path = request.getRequestURI().split("/");
        String oauthId = authentication.getName();
        log.info("path={}, oauthId={}", path, oauthId);
        Object principal = authentication.getPrincipal();
        System.out.println("principal = " + principal);

        response.sendRedirect(UriComponentsBuilder.fromUriString("http://localhost:3000/canvas")
                .queryParam("accessToken", "accessToken")
                .queryParam("refreshToken", "refreshToken")
                .build()
                .toUriString());
    }

}
