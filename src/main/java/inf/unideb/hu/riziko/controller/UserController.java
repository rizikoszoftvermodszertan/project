package inf.unideb.hu.riziko.controller;

import inf.unideb.hu.riziko.model.Lobby.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public interface UserController {
    @PutMapping("/user/create/{username}")
    User createUser(@PathVariable String username);
}
