package hbd.cakedecorating.config.auth;

import hbd.cakedecorating.config.jwt.filter.JwtAuthenticationProcessingFilter;
import hbd.cakedecorating.config.jwt.service.JwtService;
import hbd.cakedecorating.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final AuthenticationSuccessHandler MyAuthenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .csrf().disable()
                .headers().frameOptions().disable()//로컬 환경 h2 콘솔 사용
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//session 사용 x
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/**").permitAll()
                //.anyRequest().authenticated()
                .and()
                .oauth2Login()
                .successHandler(MyAuthenticationSuccessHandler)//소셜로그인 동의하고 계속하기
                .failureHandler(authenticationFailureHandler)//소셜 로그인 실패
                .userInfoEndpoint().userService(customOAuth2UserService);//소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스와 구현체를 등록한다.

        // 원래 스프링 시큐리티 필터 순서가 LogoutFilter 이후에 로그인 필터 동작
        // 따라서, LogoutFilter 이후에 우리가 만든 필터 동작하도록 설정
        http.addFilterAfter(new JwtAuthenticationProcessingFilter(jwtService, userRepository), LogoutFilter.class);

        return http.build();
    }


}
