package inf.unideb.hu.riziko.repository;

import inf.unideb.hu.riziko.controller.WebSocketController;
import inf.unideb.hu.riziko.model.GameMode;
import inf.unideb.hu.riziko.model.Lobby.Lobby;
import inf.unideb.hu.riziko.model.Lobby.User;
import inf.unideb.hu.riziko.service.LobbyService;
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
        LobbyService.getInstance().AddLobby(lobby);
        return lobby;
    }

    public Lobby joinLobby(User user, String lobbyId){
        Lobby l = getLobby(lobbyId);
        if(l.getJoinedUsers().size() == 6){
            throw new IllegalArgumentException("Can't join the lobby");
        }
        l.joinLobby(user);
        sendUpdateToLobbyMembers(l);
        LobbyService.getInstance().JoinLobby(user, lobbyId);
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
            LobbyService.getInstance().RemoveLobby(l);
        }
    }

    public void setGameMode(String lobbyId, String gameMode) {
        Lobby l = getLobby(lobbyId);
        l.setGameMode(GameMode.valueOf(gameMode));
        sendUpdateToLobbyMembers(l);
    }


    // Ezt kell meghívni ha azt szeretnénk, hogy a lobby összes tagja frissítsen
    public void sendUpdateToLobbyMembers(Lobby lobby){
        List<String> list = lobby.getJoinedUsers().stream().map(User::getUserId).toList();
        socketController.sendMessages(list, "updatelobby");
    }


    public void startGame(String lobbyId) {
        Lobby l = getLobby(lobbyId);
        l.startGame();
        sendUpdateToLobbyMembers(l);
    }
}
