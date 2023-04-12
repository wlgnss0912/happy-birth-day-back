package hbd.cakedecorating.dto.food;

import hbd.cakedecorating.model.Food;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
public class FoodListDto {

    public static class FoodListRequestDto {
        private Long id;
        private String imgName;
        private String imgPath;
        private String creSocialId;
        private LocalDateTime creDate;

        public Food toEntity() {
            return Food.builder()
                    .id(id)
                    .imgName(imgName)
                    .imgPath(imgPath)
                    .creSocialId(creSocialId)
                    .creDate(creDate)
                    .build();
        }

        @Builder
        public FoodListRequestDto(Long id, String imgName, String imgPath, String creSocialId, LocalDateTime creDate) {
            this.id = id;
            this.imgName = imgName;
            this.imgPath = imgPath;
            this.creSocialId = creSocialId;
            this.creDate = creDate;
        }
    }

    @Getter
    public static class FoodListResponseDto {
        private Long id;
        private String imgName;
        private String imgPath;
        private String creSocialId;
        private LocalDateTime creDate;

        public FoodListResponseDto(Food entity) {
            this.id = entity.getId();
            this.imgName = entity.getImgName();
            this.imgPath = entity.getImgPath();
            this.creSocialId = entity.getCreSocialId();
            this.creDate = entity.getCreDate();
        }
    }

}
