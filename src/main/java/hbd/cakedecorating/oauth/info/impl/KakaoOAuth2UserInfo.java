package hbd.cakedecorating.oauth.info.impl;

import hbd.cakedecorating.oauth.info.OAuth2UserInfo;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    public KakaoOAuth2UserInfo(Map<String, Object> attribute) {
        super(attribute);
    }

    @Override
    public String getId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getName() {
        Map<String, Object> properties = (Map<String, Object>) attribute.get("properties");

        if(properties == null) {
            return null;
        }

        return (String) attribute.get("nickname");
    }

    @Override
    public String getEmail() {
        return (String) attribute.get("account_email");
    }

    @Override
    public String getImageUrl() {
        Map<String, Object> properties = (Map<String, Object>) attribute.get("properties");

        if(properties == null) {
            return null;
        }

        return (String) attribute.get("thumbnail_image");
    }
}
