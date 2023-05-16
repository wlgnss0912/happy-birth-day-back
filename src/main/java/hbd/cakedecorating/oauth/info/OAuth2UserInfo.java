package hbd.cakedecorating.oauth.info;

import java.util.Map;

public abstract class OAuth2UserInfo {
    protected Map<String, Object> attribute;

    public OAuth2UserInfo(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    public Map<String, Object> getAttribute() {
        return attribute;
    }

    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();


}
