package hbd.cakedecorating.oauth.service;


import hbd.cakedecorating.api.entity.User;
import hbd.cakedecorating.api.repository.user.UserRepository;
import hbd.cakedecorating.oauth.entity.ProviderType;
import hbd.cakedecorating.oauth.entity.RoleType;
import hbd.cakedecorating.oauth.entity.UserPrincipal;
import hbd.cakedecorating.oauth.exception.OAuthProviderMissMatchException;
import hbd.cakedecorating.oauth.info.OAuth2UserInfo;
import hbd.cakedecorating.oauth.info.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


/**
 * loadUser 함수는 다음과 같이 작동합니다.
 * 1. OAuth2UserRequest의 인스턴스를 사용하여 OAuth 2.0 프로바이더에 요청합니다.
 * 2. OAuth 2.0 프로바이더에서 응답을 받습니다.
 * 3. 응답에서 사용자 정보를 추출합니다.
 * 4. 사용자 정보를 사용하여 OAuth2User의 인스턴스를 만듭니다.
 * 5. OAuth2User의 인스턴스를 반환합니다.
 *
 * loadUser() 메서드는 OAuth 2.0 프로바이더에서 인증된 사용자의 정보를 포함하는 OAuth2User 객체를 반환합니다. OAuth2User 객체는 다음과 같은 속성을 제공합니다.
 * - username: 인증된 사용자의 이름
 * - authorities: 인증된 사용자의 권한 목록
 * - attributes: 인증된 사용자의 속성 목록
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("OAuth2 로그인 요청 진입");

        OAuth2User user = super.loadUser(userRequest);//소셜 로그인 사용자 정보 제공 API로 요청(OAuth 서비스에서 가져온 유저 정보)

        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException ex) {//인증 에러(이름, 암호, 만료 등)
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();//발생한 예외 스택을 추적을 콘솔에 출력
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());//스프링 시큐리티 내부 오류 - 메시지, 원인 출력
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        ProviderType providerType = ProviderType.valueOf(userRequest.getClientRegistration().getClientId().toUpperCase());//GOOGLE, KAKAO

        //DefaultOAuth2UserService.getAttributes() 메서드는 인증된 사용자의 속성을 반환
            //  -> Kakao  - id, nickname, email, profile_image
            //  -> google - id, name, email, profile_picture
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
        User savedUser = userRepository.findByUserId(userInfo.getId());

        if(savedUser != null) {
            if(providerType != savedUser.getProviderType()) {
                throw new OAuthProviderMissMatchException(
                        "Looks like you're signed up with " + providerType +
                                " account. Please use your " + savedUser.getProviderType() + " account to login."
                );
            }
            updateUser(savedUser, userInfo);
        } else {
            savedUser = createUser(userInfo, providerType);
        }

        return UserPrincipal.create(savedUser, user.getAttributes());
    }

    private User createUser(OAuth2UserInfo userInfo, ProviderType providerType) {
        LocalDateTime now =  LocalDateTime.now();
        User user = User.builder()
                .userId(userInfo.getId())
                .username(userInfo.getName())
                .email(userInfo.getEmail())
                .emailVerifiedYn("Y")
                .profileImageUrl(userInfo.getImageUrl())
                .providerType(providerType)
                .roleType(RoleType.USER)
                .createdAt(now)
                .modifiedAt(now)
                .build();

        return userRepository.saveAndFlush(user);//1. 즉시 저장소에 반영되어야 하는 경우 2. 트랜잭션 내에서 엔티티의 변경 내용을 읽어야 하는 경우
    }

    private User updateUser(User savedUser, OAuth2UserInfo userInfo) {
        if(userInfo.getName() != null && savedUser.getUsername().equals(userInfo.getName())) {
            savedUser.setUsername(userInfo.getName());
        }
        if(userInfo.getImageUrl() != null && !savedUser.getProfileImageUrl().equals(userInfo.getImageUrl())) {
            savedUser.setProfileImageUrl(userInfo.getImageUrl());
        }

        return savedUser;
    }
}
