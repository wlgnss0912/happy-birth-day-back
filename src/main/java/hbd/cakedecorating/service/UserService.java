package hbd.cakedecorating.service;

import hbd.cakedecorating.config.auth.dto.CustomOAuth2User;
import hbd.cakedecorating.config.jwt.service.JwtService;
import hbd.cakedecorating.dto.UserUpdateDto;
import hbd.cakedecorating.model.user.User;
import hbd.cakedecorating.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Transactional
    public void signup(String accessToken, String birthday) {

        String nickname = jwtService.extractNickname(accessToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));

        User findUser = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("해당 Nickname과 일치하는 유저가 없습니다."))
                .authorizeUser(birthday);

        userRepository.save(findUser);
    }
}
