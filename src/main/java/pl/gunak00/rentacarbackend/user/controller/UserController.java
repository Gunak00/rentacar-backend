package pl.gunak00.rentacarbackend.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.gunak00.rentacarbackend.user.dto.UserDto;
import pl.gunak00.rentacarbackend.user.model.User;

@AllArgsConstructor
@RestController
public class UserController {

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public String getJwt(@RequestBody UserDto userDto){

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));

        User user = (User)authenticate.getPrincipal();

        System.out.println(user.toString());
        return "";
    }
}
