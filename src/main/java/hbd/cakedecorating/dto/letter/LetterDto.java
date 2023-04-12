package hbd.cakedecorating.dto.letter;

import com.querydsl.core.Tuple;
import hbd.cakedecorating.model.Food;
import hbd.cakedecorating.model.Letter;
import lombok.Data;
import lombok.Getter;

@Data
public class LetterDto {

    @Getter
    public static class LetterResponseDto {
        private Long id;
        private String context;
        private String nickname;
        private String imgName;
        private String imgPath;

        public LetterResponseDto(Long id, String context, String nickname, String imgName, String imgPath) {
            this.id = id;
            this.context = context;
            this.nickname = nickname;
            this.imgName = imgName;
            this.imgPath = imgPath;
        }
    }
}
