package hbd.cakedecorating.api.model.user;

import hbd.cakedecorating.oauth.model.ProviderType;
import hbd.cakedecorating.oauth.model.Role;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "USERS")
public class  User {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_SEQ")
    private Long userSeq;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "USERNAME")
    private String username;

    @JsonIgnore
    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "EMAIL_VERIFIED_YN")
    private String emailVerifiedYn;

    @Column(name = "BIRTHDAY")
    private String birthday;

    @Column(name = "PROVIDER_TYPE")
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Column(name = "ROLE_TYPE")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "MODIFIED_AT")
    private LocalDateTime modifiedAt;

    @Builder
    public User(String userId, String username, String email, String emailVerifiedYn, String birthday, ProviderType providerType, Role role, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.userId = userId;
        this.username = username;
        this.password = "NO_PASS";
        this.email = email != null ? email : "NO_EMAIL";
        this.emailVerifiedYn = emailVerifiedYn;
        this.birthday = birthday != null ? birthday : "";
        this.providerType = providerType;
        this.role = role;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

//    public User update(OAuth2UserInfo oAuth2UserInfo) {
//        this.nickname = oAuth2UserInfo.getName();
//        this.email = oAuth2UserInfo.getEmail();
//        this.providerId = oAuth2UserInfo.getId();
//        return this;
//    }
//
//    public String getRoleKey() {
//        return this.role.getKey();
//    }
}
