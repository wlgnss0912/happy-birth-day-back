package hbd.cakedecorating.controller;

import hbd.cakedecorating.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class KakaoController {

    private KakaoService kakaoService;

    @GetMapping("/oauth/token")
    public String getToken(String code) {
        String oauthToken = kakaoService.getAccessToken(code);
        System.out.println("code = " + code);
        return null;
    }
}
