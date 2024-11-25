package inf.unideb.hu.riziko.model.Lobby;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
public class Lobby {
    @EqualsAndHashCode.Include
    String lobbyId;
    HashSet<User> joinedUsers = new HashSet<>();
    User leader;

    public Lobby(User user){
        leader = user;
        lobbyId = UUID.randomUUID().toString();
        joinedUsers.add(user);
    }

    public void joinLobby(User user){
        joinedUsers.add(user);
        if(leader == null){
            leader = user;
        }
    }

    public void leaveLobby(String userId){
        User luser = joinedUsers.stream().filter(user -> Objects.equals(user.userId, userId)).findFirst().get();
        joinedUsers.remove(luser);
        if(Objects.equals(userId, leader.userId)){
            leader = joinedUsers.stream().findFirst().orElseGet(()->null);
        }
    }
}
