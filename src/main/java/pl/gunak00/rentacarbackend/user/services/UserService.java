package pl.gunak00.rentacarbackend.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.gunak00.rentacarbackend.user.exceptions.EmailAlreadyExistsException;
import pl.gunak00.rentacarbackend.user.model.User;
import pl.gunak00.rentacarbackend.user.repository.UserRepo;

import java.util.List;

@Service
public class UserService {
    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User addUser(User user){
        if (userRepo.existsByEmail(user.getEmail()))
            throw new EmailAlreadyExistsException("E-mail " + user.getEmail() + " jest już zarejestrowany.");

        return userRepo.save(user);
    }

    public List<User> findAllUsers(){
        return userRepo.findAll();
    }

    public User updateUser(User user){
        return userRepo.save(user);
    }

    public User findById(Long id){
        return userRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("User by id " + id + " was not found"));
    }

    public User findByEmail(String email){
        return userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User by email " + email + " was not found"));
    }

    public void deleteUser(Long id){
        userRepo.deleteById(id);
    }
}
