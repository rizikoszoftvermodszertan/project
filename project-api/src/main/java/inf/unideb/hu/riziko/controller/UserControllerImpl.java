package inf.unideb.hu.riziko.controller;

import inf.unideb.hu.riziko.model.Lobby.User;
import inf.unideb.hu.riziko.repository.UserRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserControllerImpl implements UserController {
    UserRepository userRepository;

    public UserControllerImpl(UserRepository userRepository) {this.userRepository = userRepository;}

    @Override
    public User createUser(String username) {
        User user = new User(username);
        userRepository.addUser(user);
        return user;
    }
}
