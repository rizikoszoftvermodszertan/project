package inf.unideb.hu.riziko.model.Lobby;

import inf.unideb.hu.riziko.model.GameInstance;
import inf.unideb.hu.riziko.model.Player;
import inf.unideb.hu.riziko.model.PlayerID;
import lombok.EqualsAndHashCode;
import lombok.Getter;


import java.util.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
public class Lobby {
    @EqualsAndHashCode.Include
    String lobbyId;
    HashSet<User> joinedUsers = new HashSet<>();
    User leader;
    GameInstance gameInstance;
    boolean isStarted = false;
    Map<PlayerID, String> players;
    int size;


    public Lobby(User user){
        leader = user;
        lobbyId = UUID.randomUUID().toString();
        joinedUsers.add(user);
        gameInstance = new GameInstance();
        size = joinedUsers.size();
    }

    public void joinLobby(User user){
        joinedUsers.add(user);
        if(leader == null){
            leader = user;
        }
        size = joinedUsers.size();
    }

    public void startGame(){
        if(isStarted){
            return;
        }

        isStarted = true;
        players = getPlayerIdMapping();
    }

    private Map<PlayerID, String> getPlayerIdMapping(){
        Map<PlayerID, String> playerIdMapping = new HashMap<>();

        ArrayList<String> playerIds = joinedUsers.stream().map(User::getUserId).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        for(int i = 0; i < joinedUsers.size(); i++){
            playerIdMapping.put(PlayerID.valueOf("PLAYER" + i), playerIds.get(i));
        }

        return playerIdMapping;
    }

    public void leaveLobby(String userId){
        User luser = joinedUsers.stream().filter(user -> Objects.equals(user.userId, userId)).findFirst().get();
        joinedUsers.remove(luser);
        if(Objects.equals(userId, leader.userId)){
            leader = joinedUsers.stream().findFirst().orElseGet(()->null);
        }
        size = joinedUsers.size();
    }
}
