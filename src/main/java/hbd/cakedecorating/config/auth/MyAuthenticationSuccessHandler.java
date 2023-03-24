package hbd.cakedecorating.config.auth;

import hbd.cakedecorating.config.auth.dto.CustomOAuth2User;
import hbd.cakedecorating.config.jwt.service.JwtService;
import hbd.cakedecorating.model.user.Role;
import hbd.cakedecorating.model.user.SocialType;
import hbd.cakedecorating.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static hbd.cakedecorating.model.user.Role.GUEST;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("소셜 로그인 성공!");

        CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();

        if (user.getRole() == GUEST) {
            String accessToken = jwtService.createAccessToken(user.getEmail());
            response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
            response.sendRedirect(UriComponentsBuilder.fromUriString("http://localhost:3000/addInfo")
                    .build()
                    .toUriString());

            log.info("여기 지나가용?");

            jwtService.sendAccessAndRefreshToken(response, accessToken, null);
            userRepository.findByEmail(user.getEmail())
                    .map(entity -> entity.authorizeUser())
                    .orElseThrow(() -> new IllegalArgumentException("해당 Email과 일치하는 유저가 없습니다."));
        } else {
            loginSuccess(response, user);
        }

    }

    /**
     * ※ loginSuccess 메소드 TODO
     * 현재는 이미 한 번 로그인했던 유저면 계속 토큰을 발급해주고 있는데,
     * JwtAuthenticationProcessingFilter처럼 RefreshToken의 유/무, 만기에 따라 다르게 처리하도록 필요
     */
    private void loginSuccess(HttpServletResponse response, CustomOAuth2User user) throws IOException {
        String accessToken = jwtService.createAccessToken(user.getEmail());
        String refreshToken = jwtService.createRefreshToken();
        response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
        response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(user.getEmail(), refreshToken);

        response.sendRedirect(UriComponentsBuilder.fromUriString("http://localhost:3000/canvas")
                .build()
                .toUriString());
    }

}
