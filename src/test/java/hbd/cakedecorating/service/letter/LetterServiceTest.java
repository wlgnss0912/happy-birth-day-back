package hbd.cakedecorating.service.letter;

import hbd.cakedecorating.api.service.letter.LetterService;
import hbd.cakedecorating.dto.letter.LetterDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
class LetterServiceTest {

    @Autowired
    LetterService letterService;

    @Test
    public void 편지단건조회() throws Exception {
        //given
        Long letterId = 5L;

        //when
        LetterDto.LetterResponseDto letterList = letterService.findLetter(letterId);

        //then
        assertThat(letterList.getContext()).isEqualTo("축하해!!");
    }

    @Test
    public void 편지리스트조회() throws Exception {
        //given
        String url = "test";
        Pageable pageable = PageRequest.of(0,2);

        //when
        Page<LetterDto.LetterResponseDto> letterList = letterService.findLetterList(url, pageable);

        //then
        assertThat(letterList.getContent().get(1).getImgName()).isEqualTo("애호박전");
    }
}