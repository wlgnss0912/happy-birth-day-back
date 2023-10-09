package hbd.cakedecorating.service.food;

import hbd.cakedecorating.api.service.food.FoodService;
import hbd.cakedecorating.dto.food.FoodListDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class FoodServiceTest {

    @Autowired
    FoodService foodService;

    @Test
    public void 음식리스트조회() throws Exception {
        //when
        List<FoodListDto.FoodListResponseDto> foodList = foodService.findFoodList();

        //then
        assertThat(foodList.get(0).getImgName()).isEqualTo("불고기");
        assertThat(foodList.get(1).getImgName()).isEqualTo("비빔밥");
        assertThat(foodList.get(2).getImgName()).isEqualTo("애호박전");
        assertThat(foodList.get(3).getImgName()).isEqualTo("함박스테이크");
    }

    @Test
    public void 음식그림추가() throws Exception {
        //given
        FoodListDto.FoodListRequestDto reqParam = FoodListDto.FoodListRequestDto.builder()
                .imgName("가나초콜릿")
                .imgPath("C:\\Users\\HNFincore\\Desktop\\food")
                .creSocialId("randomId")
                .creDate(LocalDateTime.now())
                .build();

        //when
        Long id = foodService.addFood(reqParam);

        //then
        assertThat(id).isEqualTo(5);
    }



}