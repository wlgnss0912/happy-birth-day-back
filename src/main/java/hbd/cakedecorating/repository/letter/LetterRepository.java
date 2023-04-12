package hbd.cakedecorating.repository.letter;

import hbd.cakedecorating.model.Letter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LetterRepository extends JpaRepository<Letter, Long>, LetterRepositoryCustom {

}
