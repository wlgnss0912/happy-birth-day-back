package hbd.cakedecorating.config.auth;

import hbd.cakedecorating.config.auth.dto.CustomOAuth2User;
import hbd.cakedecorating.config.auth.dto.OAuthAttributes;
import hbd.cakedecorating.model.user.SocialType;
import hbd.cakedecorating.model.user.User;
import hbd.cakedecorating.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static hbd.cakedecorating.model.user.SocialType.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);//소셜 로그인 사용자 정보 제공 API로 요청(OAuth 서비스에서 가져온 유저 정보)

        String registrationId = userRequest.getClientRegistration().getRegistrationId();//google, kakao
        SocialType socialType = getSocialType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();//OAuth 로그인 시 키(PK)가 되는 필드 값

        log.info("registrationId = {}", registrationId);
        log.info("socialType = {}", socialType);
        log.info("userNameAttributeName = {}", userNameAttributeName);

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(socialType, attributes);

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey(),
                user.getRole(),
                user.getNickname());
    }

    private SocialType getSocialType(String registrationId) {
        if(registrationId.equals(kakao.toString())) {
            return kakao;
        }
        return google;
    }

    private User saveOrUpdate(SocialType socialType, OAuthAttributes attributes) {
        User user = userRepository.findBySocialTypeAndSocialId(socialType, String.valueOf(attributes.getAttributes().get(attributes.getNameAttributeKey())))
                .map(entity -> entity.update(
                        attributes.getNickname()))
                .orElse(attributes.toEntity(socialType));

        return userRepository.save(user);
    }
}
