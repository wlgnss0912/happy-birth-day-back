package hbd.cakedecorating.controller;

import hbd.cakedecorating.model.user.User;
import hbd.cakedecorating.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RequiredArgsConstructor
@RestController
public class UserController {

    @Value("${jwt.access.header}")
    private String accessHeader;

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestParam String birthday, HttpServletRequest request) throws URISyntaxException {
        userService.signup(request.getHeader(accessHeader), birthday);

//        URI redirectURI = new URI("http://localhost:3000/canvas");
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setLocation(redirectURI);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
