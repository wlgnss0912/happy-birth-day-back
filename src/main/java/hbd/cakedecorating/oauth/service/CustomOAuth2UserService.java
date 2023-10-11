package hbd.cakedecorating.oauth.service;

import hbd.cakedecorating.oauth.model.ProviderType;
import hbd.cakedecorating.oauth.model.Role;
import hbd.cakedecorating.api.model.user.User;
import hbd.cakedecorating.oauth.model.UserPrincipal;
import hbd.cakedecorating.oauth.info.OAuth2UserInfo;
import hbd.cakedecorating.oauth.info.OAuth2UserInfoFactory;
import hbd.cakedecorating.api.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    //구글 로그인 버튼 클릭 -> 구글 로그인창 -> 로그인 완료 -> code를 리턴(OAuth-Clien라이브러리가 받아줌) -> code를 통해서 AcssToken요청(access토큰 받음)
    //OAuth2-client 라이브러리가 code단계 처리후 OAuth2UserRequest객체에 엑세스 토큰, 플랫폼 사용자 고유 key값을 반환해준다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest); //oauth에서 가져온 user 정보

        try {
            return process(oAuth2UserRequest, oAuth2User);//인증된 사용자 정보
        } catch (AuthenticationException ex) {//인증 예외
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());//일반 예외 - 시스템 문제로 내부 인증 관련 처리 요청 x
        }
    }

    protected OAuth2User process(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {

        //플렛폼 구분 - GOOGLE, KAKAO
        ProviderType providerType = ProviderType.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase());

        //플렛폼 별 사용자 추가정보
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, oAuth2User.getAttributes());

        //User savedUser = userRepository.findByEmail(oAuth2UserInfo.getEmail()).orElse(null);
        User savedUser = userRepository.findByUserId(oAuth2UserInfo.getId());

        //가입된 경우
        if(savedUser != null) {
            if(!savedUser.getProviderType().equals(providerType)) {
                throw new RuntimeException("Looks like you're signed up with " + providerType +
                        " account. Please use your " + savedUser.getProviderType() + " account to login.");
            }
            updateUser(savedUser, oAuth2UserInfo);
        }
        //미가입 경우
        else {
            savedUser = registerUser(providerType, oAuth2UserInfo);
        }
        return UserPrincipal.create(savedUser, oAuth2User.getAttributes());
   }

    private User registerUser(ProviderType providerType, OAuth2UserInfo oauth2UserInfo) {

        LocalDateTime now = LocalDateTime.now();

        User user = User.builder()
                .userId(oauth2UserInfo.getId())
                .username(oauth2UserInfo.getName())
                .email(oauth2UserInfo.getEmail())
                .emailVerifiedYn("Y")
                .providerType(providerType)
                .role(Role.GUEST)
                .createdAt(now)
                .modifiedAt(now)
                .build();

        return userRepository.saveAndFlush(user);//1. 즉시 저장소에 반영되어야 하는 경우 2. 트랜잭션 내에서 엔티티의 변경 내용을 읽어야 하는 경우
    }

    private User updateUser(User savedUser, OAuth2UserInfo oAuth2UserInfo) {
        if(oAuth2UserInfo.getName() != null && !savedUser.getUsername().equals(oAuth2UserInfo.getName())) {
            savedUser.setUsername(oAuth2UserInfo.getName());
        }

        return savedUser;
    }
}