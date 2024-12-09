package inf.unideb.hu.riziko.repository;

import inf.unideb.hu.riziko.model.Lobby.User;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

@Repository
public class UserRepository {
    private HashSet<User> users = new HashSet<>();

    public void addUser(User user) {
        users.add(user);
    }

    public User getUser(String userId) {
        for (User user : users) {
            if(user.getUserId().equals(userId)){
                return user;
            }
        }
        return null;
    }
}
