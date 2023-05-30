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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.gunak00.rentacarbackend.user.dto.AuthRequest;
import pl.gunak00.rentacarbackend.user.dto.AuthResponse;
import pl.gunak00.rentacarbackend.user.dto.UserDto;
import pl.gunak00.rentacarbackend.user.enums.UserRole;
import pl.gunak00.rentacarbackend.user.exceptions.EmailAlreadyExistsException;
import pl.gunak00.rentacarbackend.user.model.User;
import pl.gunak00.rentacarbackend.user.services.UserService;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;
    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, UserService userService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
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

            AuthResponse authResponse = new AuthResponse(user.getEmail(), token, user.getRole());
            return ResponseEntity.ok(authResponse);
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id){
        User user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/find/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable("email") String email){
        User user = userService.findByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody UserDto userDto){



        User user = new User(userDto.getEmail(), userDto.getFirstName(), userDto.getLastName(),
                passwordEncoder.encode(userDto.getPassword()), userDto.getDrivingLicenseNumber(),
                userDto.getAge(), UserRole.ROLE_USER.toString());

        try {
            User newUser = userService.addUser(user);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        }catch (EmailAlreadyExistsException e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }


    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user){
//        User user = new User(userDto.getEmail(), userDto.getFirstName(), userDto.getLastName(),
//                passwordEncoder.encode(userDto.getPassword()), userDto.getDrivingLicenseNumber(),
//                userDto.getAge(), UserRole.ROLE_USER.toString());


        String password = user.getPassword();
        if (!(password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$"))){
            user.setPassword(passwordEncoder.encode(password));
        }

        User updateUser = userService.updateUser(user);
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
