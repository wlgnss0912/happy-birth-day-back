package hbd.cakedecorating.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Slf4j
@RestController
public class FoodController {

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
