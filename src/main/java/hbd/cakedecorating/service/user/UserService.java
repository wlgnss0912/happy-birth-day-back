package hbd.cakedecorating.service.user;

import hbd.cakedecorating.config.jwt.service.JwtService;
import hbd.cakedecorating.model.User;
import hbd.cakedecorating.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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
