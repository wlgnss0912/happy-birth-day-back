package hbd.cakedecorating.config.security;

import hbd.cakedecorating.oauth.filter.TokenAuthenticationFilter;
import hbd.cakedecorating.oauth.handler.OAuth2AuthenticationFailureHandler;
import hbd.cakedecorating.oauth.handler.OAuth2AuthenticationSuccessHandler;
import hbd.cakedecorating.oauth.repository.CookieAuthorizationRequestRepository;
import hbd.cakedecorating.oauth.service.CustomOAuth2UserService;
import hbd.cakedecorating.oauth.service.CustomUserDetailsService;
import hbd.cakedecorating.oauth.token.AuthTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final CustomOAuth2UserService oAuth2UserService;
    private final AuthTokenProvider tokenProvider;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    /**
     * UserDetailsService 설정 - 인증에 대한 지원
     * OAuth2를 사용하여 소셜 계정으로 로그인하면 CustomOAuth2UserService 클래스가 OAuth2 제공업체에서 제공하는 사용자 정보를 사용하여 인증된 사용자 정보를 생성합니다.
     * 이 인증된 사용자 정보는 UserDetailsService 인터페이스를 통해 사용자에게 로그인 권한을 부여하는 데 사용됩니다.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //httpBasic, csrf, formLogin, rememberMe, logout, session disable
        http
                .cors()
                .and()
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .rememberMe().disable()//로그인 상태 유지 X
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //요청에 대한 권한 설정
        http.authorizeHttpRequests()
                .requestMatchers("/oauth2/**").permitAll()// CORS 사전 요청
                .anyRequest().authenticated();

        //OAuth 2.0 로그인을 활성화
        http.oauth2Login()
                .authorizationEndpoint().baseUri("/oauth2/authorization")//OAuth 2.0 인증 요청을 처리하는 URL을 지정
                .authorizationRequestRepository(cookieAuthorizationRequestRepository)//인증 요청을 cookie 에 저장 (기본적으로는 HttpSessionOAuth2... 사용)
                .and()
                .redirectionEndpoint().baseUri("/*/oauth2/code/*")//소셜 인증 후 redirect url - 액세스 토큰을 받기 위해 리디렉션(인가)
                .and()
                .userInfoEndpoint().userService(oAuth2UserService)//가져온 사용자 정보를 사용할 서비스 class 지정 //userService()는 OAuth2 인증 과정에서 Authentication 생성에 필요한 OAuth2User 를 반환하는 클래스를 지정한다.
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler) //oauth 인증 성공(동의하고 계속하기) 시 호출되는 handler
                .failureHandler(oAuth2AuthenticationFailureHandler);//oauth 인증 실패 시 호출되는 handler

        http.logout()
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID");

        //jwt filter 설정
        http.addFilterBefore(new TokenAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

    /*
     * security 설정 시, 사용할 인코더 설정
     * */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
