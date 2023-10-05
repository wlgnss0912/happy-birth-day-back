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
 * GenericFilterBean은 더 일반적인 필터를 만들 때 사용되며,
 * OncePerRequestFilter는 한 번만 실행해야 하는 필터를 만들 때 사용됩니다.
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
        }

        filterChain.doFilter(request, response);
    }
}
