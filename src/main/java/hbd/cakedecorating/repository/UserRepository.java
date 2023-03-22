package hbd.cakedecorating.repository;

import hbd.cakedecorating.model.user.SocialType;
import hbd.cakedecorating.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
    Optional<User> findByRefreshToken(String refreshToken);

    /**
     * 소셜 타입과 소셜 식별값으로 회원을 찾는 메소드
     * [추가 정보]를 입력 받기위해서 해당 회원을 찾기 위함
     */
    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
}
