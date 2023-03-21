package pl.gunak00.rentacarbackend.configuration;

import com.auth0.jwt.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.gunak00.rentacarbackend.user.enums.UserRole;
import pl.gunak00.rentacarbackend.user.model.User;
import pl.gunak00.rentacarbackend.user.repository.UserRepo;

@Configuration
public class SecurityConfiguration {

    private final UserRepo userRepo;
    private final JwtTokenFilter jwtTokenFilter;

    private final String[] requestForAll = { "/car/all", "/car/find/*", "/user/login", "/user/add", "user/find/*"};

    @Autowired
    public SecurityConfiguration(UserRepo userRepo, JwtTokenFilter jwtTokenFilter) {
        this.userRepo = userRepo;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    //To refactor
//    @EventListener(ApplicationReadyEvent.class)
//    public void saveUser(){
//        User user = new User("pkonwa00@gmail.com", getBcryptPasswordEncoder().encode("admin123"), UserRole.ROLE_ADMIN.toString());
//        User user2 = new User("anna@gmail.com", getBcryptPasswordEncoder().encode("qwertyuiop"), UserRole.ROLE_USER.toString());
//        userRepo.save(user);
//        userRepo.save(user2);
//    }

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with email not found: " + username));
    }

    @Bean
    public PasswordEncoder getBcryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeHttpRequests()
                .requestMatchers(requestForAll).permitAll()
                .anyRequest().hasRole("ADMIN");
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
