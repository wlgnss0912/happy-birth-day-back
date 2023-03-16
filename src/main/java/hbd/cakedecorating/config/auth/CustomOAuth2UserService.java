package hbd.cakedecorating.config.auth;

import hbd.cakedecorating.config.auth.dto.OAuthAttributes;
import hbd.cakedecorating.config.auth.dto.SessionUser;
import hbd.cakedecorating.model.user.Member;
import hbd.cakedecorating.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);//OAuth 서비스(google, kakao)에서 가져온 유저 정보를 담고 있음.

        String registrationId = userRequest.getClientRegistration().getRegistrationId();//OAuth의 이름 (google, kakao...)
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();//OAuth 로그인 시 키(PK)가 되는 필드 값 //google의 경우 기존적으로 "sub"로 지원하지만, 카카오 네이버 등은 기본 지원 x

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());//OAuth2UserService를 통해서 가져온 OAuth2User의 attribute를 담고있는 클래스(카카오도 이 클래스 사용)

        Member user = saveOrUpdate(attributes);//사용자의 정보가 없는경우 db insert / 있는 경우 업데이트 되었을 때를 대비해서 update기능 추가

        httpSession.setAttribute("user", new SessionUser(user)); //세션에 사용자 정보 저장

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private Member saveOrUpdate(OAuthAttributes attributes) {
        Member user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);//db 저장
    }
}
