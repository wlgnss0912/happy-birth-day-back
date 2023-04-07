package hbd.cakedecorating.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class  User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String nickname;

    private String birthday;
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "social_type")
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(name = "social_id")
    private String socialId;//로그인한 소셜 타입의 식별자 값

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    public User authorizeUser(String birthday) {
        this.role = Role.USER;
        this.birthday = birthday;
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
