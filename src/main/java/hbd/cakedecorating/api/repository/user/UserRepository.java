package hbd.cakedecorating.api.repository.user;

import hbd.cakedecorating.api.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(String userId);
    Optional<User> findByEmail(String email);
}
