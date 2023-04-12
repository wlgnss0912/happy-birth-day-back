package hbd.cakedecorating.repository.user;

import hbd.cakedecorating.model.SocialType;
import hbd.cakedecorating.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNickname(String nickname);
    Optional<User> findByRefreshToken(String refreshToken);

    /**
     * 소셜 타입과 소셜 식별값으로 회원을 찾는 메소드
     * [추가 정보]를 입력 받기위해서 해당 회원을 찾기 위함
     */
    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
}
