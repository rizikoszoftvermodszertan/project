package inf.unideb.hu.riziko.repository;

import inf.unideb.hu.riziko.controller.WebSocketController;
import inf.unideb.hu.riziko.model.Lobby.Lobby;
import inf.unideb.hu.riziko.model.Lobby.User;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Repository
public class LobbyRepository {
    WebSocketController socketController;

    public LobbyRepository(WebSocketController webSocketController) {this.socketController = webSocketController;}

    private HashSet<Lobby> lobbies = new HashSet<>();

    public Lobby createLobby(User user){
        Lobby lobby = new Lobby(user);
        lobbies.add(lobby);
        return lobby;
    }

    public Lobby joinLobby(User user, String lobbyId){
        Lobby l = lobbies.stream().filter(lobby -> lobby.getLobbyId().equals(lobbyId)).findFirst().get();
        l.joinLobby(user);
        sendUpdateToLobbyMembers(l);
        return l;
    }

    public Lobby getLobby(String lobbyId) {
        Lobby l = lobbies.stream()
                .filter(lobby -> Objects.equals(lobby.getLobbyId(), lobbyId))
                .findFirst()
                .get();
        return l;
    }

    public void leaveLobby(User user){
        Lobby l = lobbies.stream().filter(lobby -> lobby.getJoinedUsers().contains(user)).findFirst().get();
        sendUpdateToLobbyMembers(l);
        l.leaveLobby(user.getUserId());
        if((long) l.getJoinedUsers().size() == 0){
            lobbies.remove(l);
        }
    }


    // Ezt kell meghívni ha azt szeretnénk, hogy a lobby összes tagja frissítsen
    private void sendUpdateToLobbyMembers(Lobby lobby){
        List<String> list = lobby.getJoinedUsers().stream().map(User::getUserId).toList();
        socketController.sendMessages(list, "updatelobby");
    }

}
