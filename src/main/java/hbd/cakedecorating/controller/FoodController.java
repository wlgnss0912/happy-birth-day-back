package hbd.cakedecorating.controller;

import hbd.cakedecorating.dto.food.FoodListDto;
import hbd.cakedecorating.service.food.FoodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FoodController {

    private final FoodService foodService;

    @GetMapping("/api/food")
    public ResponseEntity<Object> foodList() {

        List<FoodListDto.FoodListResponseDto> foodList = foodService.findFoodList();

        return ResponseEntity.status(HttpStatus.OK).body(foodList);
    }

    @PostMapping("/api/food")
    public ResponseEntity<Object> createFood(@RequestBody FoodListDto.FoodListRequestDto foodParams) {

        Long foodId = foodService.addFood(foodParams);

        return ResponseEntity.status(HttpStatus.OK).body(foodId);
    }

    @PostMapping("/api/saveImage")
    public ResponseEntity<Object> saveImage(@RequestParam(value = "file") MultipartFile[] files) {

        String uploadFolder = "C:\\Users\\HNFincore\\Desktop\\FOODIMG";

        for (MultipartFile multipartFile : files) {
            log.info("----------------------------");
            log.info("Upload File Name={} ", multipartFile.getOriginalFilename());
            log.info("file size={}", files[0].getSize());

            File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());

            try {
                multipartFile.transferTo(saveFile);//파일의 저장
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
