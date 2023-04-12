package hbd.cakedecorating.repository.letter;

import hbd.cakedecorating.dto.letter.LetterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LetterRepositoryCustom {
    LetterDto.LetterResponseDto findLettersById(Long letterId);
    Page<LetterDto.LetterResponseDto> findLettersByTableId(Long tableId, Pageable pageable);
}
