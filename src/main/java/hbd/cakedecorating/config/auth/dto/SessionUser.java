package hbd.cakedecorating.config.auth.dto;

import hbd.cakedecorating.model.user.Member;
import lombok.Getter;

@Getter
public class SessionUser {
    String name;
    String email;
    String picture;

    public SessionUser(Member user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
