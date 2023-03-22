package hbd.cakedecorating.config.auth.dto;

import hbd.cakedecorating.model.user.User;
import lombok.Getter;

@Getter
public class SessionUser {
    String name;
    String email;

    public SessionUser(User user) {
        this.name = user.getNickname();
        this.email = user.getEmail();
    }
}
