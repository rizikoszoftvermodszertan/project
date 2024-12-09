package inf.unideb.hu.riziko.model.Lobby;

import inf.unideb.hu.riziko.model.GameInstance;
import inf.unideb.hu.riziko.model.GameMode;
import inf.unideb.hu.riziko.model.Player;
import inf.unideb.hu.riziko.model.PlayerID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


import java.util.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
public class Lobby {
    @EqualsAndHashCode.Include
    String lobbyId;
    @Getter
    HashSet<User> joinedUsers = new HashSet<>();
    User leader;
    GameInstance gameInstance;
    @Setter
    GameMode gameMode;
    boolean isStarted = false;
    Map<String, PlayerID> players;
    int size;


    public Lobby(User user){
        leader = user;
        lobbyId = UUID.randomUUID().toString();
        joinedUsers.add(user);
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
        gameInstance = new GameInstance(gameMode, size, "/data/maps/world");
        if(isStarted){
            return;
        }

        isStarted = true;
        players = getPlayerIdMapping();
    }

    private Map<String, PlayerID> getPlayerIdMapping(){
        Map<String, PlayerID> playerIdMapping = new HashMap<>();

        ArrayList<String> playerIds = joinedUsers.stream().map(User::getUserId).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        for(int i = 0; i < joinedUsers.size(); i++){
            playerIdMapping.put(playerIds.get(i), PlayerID.valueOf("PLAYER" + i));
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

    public PlayerID getPlayerId(String UserID)
    {
        return players.get(UserID);
    }
}
