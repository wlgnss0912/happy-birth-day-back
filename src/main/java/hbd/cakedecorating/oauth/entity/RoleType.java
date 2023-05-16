package hbd.cakedecorating.oauth.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum RoleType {
    USER("ROLE_USER", "회원"),
    GUEST("ROLE_GUEST", "추가정보 입력 필요 사용자");

    private final String code;
    private final String displayName;

    public static RoleType of(String code) {
        return Arrays.stream(RoleType.values())
                .filter(r -> r.getCode().equals(code))
                .findAny()//첫번째 인스턴스를 찾음
                .orElse(GUEST);//없으면 Guest 반환
    }
}
