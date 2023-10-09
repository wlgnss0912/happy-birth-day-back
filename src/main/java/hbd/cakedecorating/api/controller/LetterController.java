package hbd.cakedecorating.api.controller;

import hbd.cakedecorating.dto.letter.LetterDto;
import hbd.cakedecorating.api.service.letter.LetterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LetterController {

    private final LetterService letterService;

    @GetMapping("/api/letter")
    public ResponseEntity<Object> letterList(Pageable pageable) {

        Page<LetterDto.LetterResponseDto> letterList = letterService.findLetterList("test", pageable);

        return ResponseEntity.status(HttpStatus.OK).body(letterList);
    }

    @GetMapping("/api/letter/{id}")
    public ResponseEntity<Object> letter(@PathVariable("id") Long id) {

        LetterDto.LetterResponseDto letter = letterService.findLetter(id);

        return ResponseEntity.status(HttpStatus.OK).body(letter);
    }
}
