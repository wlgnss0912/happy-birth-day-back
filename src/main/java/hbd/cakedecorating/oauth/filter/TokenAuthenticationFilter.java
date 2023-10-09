package hbd.cakedecorating.oauth.filter;

import hbd.cakedecorating.oauth.token.AuthToken;
import hbd.cakedecorating.oauth.token.AuthTokenProvider;
import hbd.cakedecorating.utils.HeaderUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 어느 서블릿 컨테이너에서나 요청 당 한 번의 실행을 보장하는 것을 목표로 한다.
 * 동일한 request안에서 한번만 필터링을 할 수 있게 해주는 것이 OncePerRequestFilter의 역할이고
 * 보통 인증 또는 인가와 같이 한번만 거쳐도 되는 로직에서 사용
 */

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final AuthTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = HeaderUtils.getAccessToken(request);
        AuthToken authToken = tokenProvider.convertAuthToken(accessToken);

        if(authToken.validate()) {
            Authentication authentication = tokenProvider.getAuthentication(authToken);// 인증된 사용자
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug(authentication.getName() + "의 인증정보 저장.");
        } else {
            log.debug("not exists invalid jwt token.");
        }

        filterChain.doFilter(request, response);
    }
}
