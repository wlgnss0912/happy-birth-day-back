package hbd.cakedecorating.config.security;

import hbd.cakedecorating.oauth.entity.RoleType;
import hbd.cakedecorating.oauth.exception.RestAuthenticationEntryPoint;
import hbd.cakedecorating.oauth.handler.OAuth2AuthenticationFailureHandler;
import hbd.cakedecorating.oauth.handler.OAuth2AuthenticationSuccessHandler;
import hbd.cakedecorating.oauth.handler.TokenAccessDeniedHandler;
import hbd.cakedecorating.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import hbd.cakedecorating.oauth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService oAuth2UserService;
    private final TokenAccessDeniedHandler tokenAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//세션을 spring security가 생성하지 않음. spring security는 클라이언트 요청마다 인증을 요구
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()//클라이언트가 사용자 이름과 비밀번호를 http 요청에 포함해서 인증하는 방식 (사용 x)
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())//인증 실패시 호출되는 핸들러
                .accessDeniedHandler(tokenAccessDeniedHandler)//사용자에게 권한이 없음을 알리기 위해 사용
                .and()
                .authorizeHttpRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()// CORS 사전 요청
                .requestMatchers("/api/**").hasAnyAuthority(RoleType.USER.getCode())//수정 필요할 것 같음
                .anyRequest().authenticated()
                .and()
                .oauth2Login()//OAuth 2.0 로그인을 활성화
                .authorizationEndpoint()
                .baseUri("/oauth2/authorization")//OAuth 2.0 인증 요청을 처리하는 URL을 지정
                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())//OAuth 2.0 인증 요청을 저장하는 저장소를 지정
                .and()
                .redirectionEndpoint()//리다이렉션 끝점
                .baseUri("/*/oauth2/code/*")//클라이언트는 사용자로부터 액세스 토큰을 받기 위해 리디렉션
                .and()
                .userInfoEndpoint()//사용자 정보의 끝점
                .userService(oAuth2UserService)//사용자 정보를 가져온데 사용할 서비스 class 지정
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler())
                .failureHandler(oAuth2AuthenticationFailureHandler());

        return http.build();

    }

    //쿠키 기반 인가 repository, 인가 응답을 연계 하고 검증할 때 사용
    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    /*
     * Oauth 인증 성공 핸들러
     * */
    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(
                tokenProvider,
                appProperties,
                userRefreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository()
        );
    }

    /*
     * Oauth 인증 실패 핸들러
     * */
    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(oAuth2AuthorizationRequestBasedOnCookieRepository());
    }


}
