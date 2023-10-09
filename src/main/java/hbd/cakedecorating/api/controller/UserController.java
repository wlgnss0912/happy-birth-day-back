package hbd.cakedecorating.api.controller;

import hbd.cakedecorating.dto.user.UserUpdateDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RequiredArgsConstructor
@RestController
public class UserController {

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@Validated @RequestBody UserUpdateDto birthday,
                                         HttpServletRequest request,
                                         BindingResult bindingResult) throws URISyntaxException {

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
