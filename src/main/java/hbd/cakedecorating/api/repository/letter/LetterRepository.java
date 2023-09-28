package hbd.cakedecorating.api.repository.letter;

import hbd.cakedecorating.api.model.Letter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LetterRepository extends JpaRepository<Letter, Long>, LetterRepositoryCustom {

}
