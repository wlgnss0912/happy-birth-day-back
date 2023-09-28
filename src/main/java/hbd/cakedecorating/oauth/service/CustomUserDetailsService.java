package hbd.cakedecorating.oauth.service;

import hbd.cakedecorating.api.model.user.User;
import hbd.cakedecorating.api.repository.user.UserRepository;
import hbd.cakedecorating.oauth.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserDetailsService는 스프링 시큐리티에서 사용자 인증을 처리하는 데 사용되는 인터페이스입니다.
 * UserDetailsService 구현체는 사용자의 사용자 이름과 암호를 사용하여 -> 인증된 사용자 정보를 반환
 */
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(username);
        if(user == null) {
            throw new UsernameNotFoundException("Can not find username.");
        }
        return UserPrincipal.create(user);//인증된 사용자 정보 생성 및 반환 - (사용자 이름 권한 암호 등의 정보 포함)
    }
}
