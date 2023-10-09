package hbd.cakedecorating.api.service.diningTable;

import hbd.cakedecorating.api.model.DiningTable;
import hbd.cakedecorating.api.repository.diningTable.DiningTableRepository;
import hbd.cakedecorating.api.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiningTableService {

    private final UserRepository userRepository;
    private final DiningTableRepository diningTableRepository;

    /**
     * 밥상 주인 이름 찾기
     */
    public String findDiningTableUser(String personalUrl) {
        DiningTable diningTable = diningTableRepository.findByUrl(personalUrl);
        String nickname = diningTable.getUser().getUsername();
        return nickname;
    }

    /**
     * 밥상 생성
     */
//    @Transactional
//    public Long createDiningTable(String accessToken) {
//        //엔티티조회 - 회원
//        String nickname = jwtService.extractNickname(accessToken)
//                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));
//
//        User user = userRepository.findByNickname(nickname)
//                .orElseThrow(() -> new IllegalArgumentException("해당 Nickname과 일치하는 유저가 없습니다."));
//
//        //개인 url 생성
//        String personalUrl = RandomStringUtils.randomAlphabetic(10);
//
//        DiningTable diningTable = DiningTable.createDiningTable(user, personalUrl);
//
//        diningTableRepository.save(diningTable);
//
//        return diningTable.getUser().getId();
//    }

}
