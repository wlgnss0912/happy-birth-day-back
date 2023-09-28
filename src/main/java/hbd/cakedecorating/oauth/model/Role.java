package hbd.cakedecorating.oauth.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER("ROLE_USER", "회원"),
    ADMIN("ROLE_ADMIN", "관리자"),
    GUEST("GUEST", "게스트");

    private final String key;
    private final String titie;

    public static Role of(String key) {
        return Arrays.stream(Role.values())//배열을 가지고 stream 반환
                .filter(r -> r.getKey().equals(key))
                .findAny()
                .orElse(GUEST);
    }
}
