package hbd.cakedecorating.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserUpdateDto {

    @NotBlank(message = "생년월일을 입력해 주세요.")
    @Pattern(regexp = "(19|20)\\\\d{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])", message = "생년월일 형식(yyyymmdd)에 맞지 않습니다.")
    private String birthday;
}
