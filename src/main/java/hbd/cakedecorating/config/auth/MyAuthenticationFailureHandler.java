package hbd.cakedecorating.config.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
      log.info("social Login fail! error={}", exception.getMessage());

      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().write("소셜 로그인 실패! 서버 로그를 확인해 주세요.");
      response.sendRedirect(UriComponentsBuilder.fromUriString("http://localhost:3000")
              .toUriString());
    }
}
