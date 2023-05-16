package hbd.cakedecorating.oauth.info.impl;

import hbd.cakedecorating.oauth.info.OAuth2UserInfo;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attribute) {
        super(attribute);//부모 class의 생성자를 호출(자식 class의 생성자에서만 사용가능)
    }

    @Override
    public String getId() {
        return (String) attribute.get("sub");
    }

    @Override
    public String getName() {
        return (String) attribute.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attribute.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) attribute.get("picture");
    }
}
