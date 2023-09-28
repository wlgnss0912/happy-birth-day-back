package hbd.cakedecorating.service.letter;

import hbd.cakedecorating.dto.letter.LetterDto;
import hbd.cakedecorating.api.model.DiningTable;
import hbd.cakedecorating.api.repository.diningTable.DiningTableRepository;
import hbd.cakedecorating.api.repository.letter.LetterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LetterService {

    private final LetterRepository letterRepository;
    private final DiningTableRepository diningTableRepository;

    /**
     * 편지 단건 조회
     */
    public LetterDto.LetterResponseDto findLetter(Long letterId) {
        LetterDto.LetterResponseDto findLetter = letterRepository.findLettersById(letterId);
        return findLetter;
    }

    /**
     * 편리 리스트 및 단건 조회
     */
    public Page<LetterDto.LetterResponseDto> findLetterList(String url, Pageable pageable) {

        DiningTable findDiningTable = diningTableRepository.findByUrl(url);
        Long tableId = findDiningTable.getId();

        Page<LetterDto.LetterResponseDto> letters = letterRepository.findLettersByTableId(tableId, pageable);
        return letters;
    }
}
