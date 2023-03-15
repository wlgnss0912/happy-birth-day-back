package hbd.cakedecorating.service;

import hbd.cakedecorating.repository.KakaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoServiceImpl implements KakaoService {

    private KakaoRepository kakaoRepository;

    @Override
    public String getAccessToken(String code) {


        return null;
    }
}
