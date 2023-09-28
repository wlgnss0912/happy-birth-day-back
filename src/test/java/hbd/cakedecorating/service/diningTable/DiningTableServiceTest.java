package hbd.cakedecorating.service.diningTable;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DiningTableServiceTest {

    @Autowired DiningTableService diningTableService;
    //@Autowired JwtService jwtService;

    @Test
    public void 밥상생성() throws Exception {
        //given
        //String accessToken = jwtService.createAccessToken("쮸니");

        //when
        //Long diningTableId = diningTableService.createDiningTable(accessToken);

        //then
        //assertThat(diningTableId).isEqualTo(1);
    }

    @Test
    public void 밥상주인찾기() throws Exception {
        //given
        String personalUrl = "test";

        //when
        String diningTableUser = diningTableService.findDiningTableUser(personalUrl);

        //then
        assertThat(diningTableUser).isEqualTo("쮸니");
    }
}