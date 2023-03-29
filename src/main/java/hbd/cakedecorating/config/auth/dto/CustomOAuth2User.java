package hbd.cakedecorating.config.auth.dto;

import hbd.cakedecorating.model.user.Role;
import hbd.cakedecorating.model.user.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * 추가정보 입력을 위해 DefaultOAuth2User를 커스텀할 것이다.
 * role 필드를 추가로 가진다.
 * email은 고려
 */
@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private Role role;
    private String nickname;


    /**
     * Constructs a {@code DefaultOAuth2User} using the provided parameters.
     *
     * @param authorities      the authorities granted to the user
     * @param attributes       the attributes about the user
     * @param nameAttributeKey the key used to access the user's &quot;name&quot; from
     *                         {@link #getAttributes()}
     */
    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey,
                            Role role, String nickname) {
        super(authorities, attributes, nameAttributeKey);
        this.role = role;
        this.nickname = nickname;
    }
}
