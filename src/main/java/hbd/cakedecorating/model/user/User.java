package hbd.cakedecorating.model.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    private String birthday;
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialId;//로그인한 소셜 타입의 식별자 값

    private String refreshToken;

    public User authorizeUser() {
        this.role = Role.USER;
        return this;
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

    @Builder
    public User(String nickname, String birthday, String email, Role role, SocialType socialType, String socialId, String refreshToken) {
        this.nickname = nickname;
        this.birthday = birthday;
        this.email = email;
        this.role = role;
        this.socialType = socialType;
        this.socialId = socialId;
        this.refreshToken = refreshToken;
    }

    public User update(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
