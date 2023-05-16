package hbd.cakedecorating.controller;

import hbd.cakedecorating.dto.user.UserUpdateDto;
import hbd.cakedecorating.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RequiredArgsConstructor
@RestController
public class UserController {

    @Value("${jwt.access.header}")
    private String accessHeader;

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@Validated @RequestBody UserUpdateDto birthday,
                                         HttpServletRequest request,
                                         BindingResult bindingResult) throws URISyntaxException {

        userService.signup(request.getHeader(accessHeader), birthday.getBirthday());

//        URI redirectURI = new URI("http://localhost:3000/canvas");
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setLocation(redirectURI);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
