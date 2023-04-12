package hbd.cakedecorating.service.food;

import hbd.cakedecorating.dto.food.FoodListDto;
import hbd.cakedecorating.model.Food;
import hbd.cakedecorating.repository.food.FoodRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;

    /**
     * 음식 리스트 조회
     */
    public List<FoodListDto.FoodListResponseDto> findFoodList() {
        List<Food> foodList = foodRepository.findAll();

        return foodList.stream()
                .map(FoodListDto.FoodListResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 음식 추가하기
     */
    @Transactional
    public Long addFood(FoodListDto.FoodListRequestDto params) {
        Food food = foodRepository.save(params.toEntity());
        return food.getId();
    }

    /**
     * 이미지 저장
     */
    public void saveImage() {
        String imgServerUrl = "https://blog.kakaocdn.net/dn/rHsBM/btqZyvtUJwc/NXxAFyKR6tzAXOUaYJNi3k/img.png";

        try {
            URL imgUrl = new URL(imgServerUrl);
            String extension = imgServerUrl.substring(imgServerUrl.lastIndexOf(".") + 1);//확장자
            String fileName = "이미지 이름";

            BufferedImage image = ImageIO.read(imgUrl);
            File file = new File("myImage/" + fileName + "." + extension);
            if(!file.exists()) { // 해당 경로의 폴더가 존재하지 않을 경우
                file.mkdirs(); // 해당 경로의 폴더 생성
            }

            ImageIO.write(image, extension, file); // image를 file로 업로드
            System.out.println("이미지 업로드 완료!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

