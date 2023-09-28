package hbd.cakedecorating.Querydsl;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class QuerydslApplicationTest {

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("querydsl 세팅 테스트")
    public void contextLoad() {

    }
}
