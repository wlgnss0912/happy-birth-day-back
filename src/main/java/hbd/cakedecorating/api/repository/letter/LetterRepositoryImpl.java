package hbd.cakedecorating.api.repository.letter;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hbd.cakedecorating.dto.letter.LetterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static hbd.cakedecorating.api.model.QFood.food;
import static hbd.cakedecorating.api.model.QLetter.letter;

@RequiredArgsConstructor
public class LetterRepositoryImpl implements LetterRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public LetterDto.LetterResponseDto findLettersById(Long letterId) {
        return queryFactory
                .select(Projections.constructor(
                        LetterDto.LetterResponseDto.class,
                        letter.id,
                        letter.context,
                        letter.nickname,
                        food.imgName,
                        food.imgPath))
                .from(letter)
                .join(letter.food, food)
                .where(letter.id.eq(letterId))
                .fetchOne();
    }

    @Override
    public Page<LetterDto.LetterResponseDto> findLettersByTableId(Long tableId, Pageable pageable) {
        List<LetterDto.LetterResponseDto> content = queryFactory
                .select(Projections.constructor(
                        LetterDto.LetterResponseDto.class,
                        letter.id,
                        letter.context,
                        letter.nickname,
                        food.imgName,
                        food.imgPath))
                .from(letter)
                .leftJoin(letter.food, food)
                .where(letter.diningTable.id.eq(tableId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(letter.count())
                .from(letter)
                .leftJoin(letter.food, food)
                .where(letter.diningTable.id.eq(tableId));

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchOne());
    }
}
