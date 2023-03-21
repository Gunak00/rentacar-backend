package pl.gunak00.rentacarbackend.user.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import pl.gunak00.rentacarbackend.user.dto.AuthRequest;
import pl.gunak00.rentacarbackend.user.dto.AuthResponse;
import pl.gunak00.rentacarbackend.user.model.User;


@RestController
@RequestMapping("/auth")
public class UserController {

    private final AuthenticationManager authenticationManager;
    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    public UserController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> getJwt(@RequestBody AuthRequest authRequest) {

        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

            User user = (User) authenticate.getPrincipal();

            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("Piotr Konwa")
                    .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                    .sign(algorithm);

            AuthResponse authResponse = new AuthResponse(user.getEmail(), token);
            return ResponseEntity.ok(authResponse);
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }
}
