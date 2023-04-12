package hbd.cakedecorating.config.jwt.filter;

import hbd.cakedecorating.config.jwt.service.JwtService;
import hbd.cakedecorating.model.User;
import hbd.cakedecorating.repository.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().equals("/login")) {
            filterChain.doFilter(request, response); // "/login" 요청이 들어오면, 다음 필터 호출
            return; // return으로 이후 현재 필터 진행 막기 (안해주면 아래로 내려가서 계속 필터 진행시킴)
        }

        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        /**
         * RefreshToken 헤더에 포함(AccessToken이 만료 되었다는 의미)
         * DB에서 user를 찾고 AccessToken과 RefreshToken 재발급
         */
        if(refreshToken != null) {
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return;
        }

        /**
         * RefreshToken 유효 함으로 AccessToken만 검증
         */
        if (refreshToken == null) {
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }

        log.info("url={}", request.getRequestURI());
        log.info("refreshToken={}", refreshToken);
    }

    /**
     * AccessToken 체크
     * 인증 처리
     */
    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .ifPresent(accessToken -> jwtService.extractNickname(accessToken)
                        .ifPresent(nickname -> userRepository.findByNickname(nickname)
                                .ifPresent(this::saveAuthentication)));

        filterChain.doFilter(request, response);//다음 필터로 넘겨줌
    }

    private void saveAuthentication(User user) {

        String password = "ds213a4d4f3a2323j4k32njk4235lv";

        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(user.getNickname())
                .password(password)
                .roles(user.getRole().name())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * RefreshToken으로 유저 정보 찾기
     * AccessToken / RefreshToken 재발급 이후 응답 헤더에 보내기
     */
    private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        userRepository.findByRefreshToken(refreshToken)
                .ifPresent(user -> {
                    String reIssueRefreshToken = reIssueRefreshToken(user);
                    jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(user.getNickname()), reIssueRefreshToken);
                });
    }

    /**
     * RefreshToken 재발급
     * DB에 RefreshToken 업데이트
     */
    @Transactional
    private String reIssueRefreshToken(User user) {
        String reIssuedRefreshToken = jwtService.createRefreshToken();
        user.updateRefreshToken(reIssuedRefreshToken);
        userRepository.saveAndFlush(user);
        return reIssuedRefreshToken;
    }
}
