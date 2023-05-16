package hbd.cakedecorating.oauth.info;

import hbd.cakedecorating.oauth.entity.ProviderType;
import hbd.cakedecorating.oauth.info.impl.GoogleOAuth2UserInfo;
import hbd.cakedecorating.oauth.info.impl.KakaoOAuth2UserInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(ProviderType providerType, Map<String, Object> attributes) {
        switch (providerType) {
            case GOOGLE -> {
                return new GoogleOAuth2UserInfo(attributes);
            }
            case KAKAO -> {
                return new KakaoOAuth2UserInfo(attributes);
            }
            default -> throw new IllegalArgumentException("Invalid Provider Type.");//부적절한 인수가 전달 되었음 (예외)
        }
    }
}
