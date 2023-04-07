package hbd.cakedecorating.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("ROLE_GUEST", "추가정보 입력 필요 사용자"),
    USER("ROLE_USER", "회원");

    private final String key;
    private final String titie;
}
