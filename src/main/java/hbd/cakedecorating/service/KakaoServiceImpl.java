package hbd.cakedecorating.service;

import hbd.cakedecorating.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoServiceImpl implements KakaoService {

    private UserRepository USerRepository;

    @Override
    public String getAccessToken(String code) {


        return null;
    }
}
