package hbd.cakedecorating.repository;

import hbd.cakedecorating.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoRepository extends JpaRepository<User, Long> {

    public User findByEmail(String email);
}
