package hbd.cakedecorating.config.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

//public class CustomJsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
//
//    private static final String DEFAULT_LOGIN_REQUEST_URL = "/login"; // "/login"으로 오는 요청을 처리
//    private static final String HTTP_METHOD = "POST"; // 로그인 HTTP 메소드는 POST
//    private static final String CONTENT_TYPE = "application/json"; // JSON 타입의 데이터로 오는 로그인 요청만 처리
//    private static final String USERNAME_KEY = "email"; // 회원 로그인 시 이메일 요청 JSON Key : "email"
//    private static final String PASSWORD_KEY = "password"; // 회원 로그인 시 비밀번호 요청 JSon Key : "password"
//    private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER =
//            new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD); // "/login" + POST로 온 요청에 매칭된다.
//
//    private final ObjectMapper objectMapper;
//
//    protected CustomJsonUsernamePasswordAuthenticationFilter(String defaultFilterProcessesUrl, ObjectMapper objectMapper) {
//        super(defaultFilterProcessesUrl);
//        this.objectMapper = objectMapper;
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
//        return null;
//    }
//}
