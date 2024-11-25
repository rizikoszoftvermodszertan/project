package inf.unideb.hu.riziko.controller;

import inf.unideb.hu.riziko.model.Lobby.Lobby;
import inf.unideb.hu.riziko.model.Lobby.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Controller
public interface LobbyController {

    @PostMapping("/lobby/create")
    String createLobby(@RequestBody String userId);
    @PostMapping("/lobby/{lobbyId}/join")
    void joinLobby(@RequestBody String userId, @PathVariable String lobbyId);
    @GetMapping("/lobby/{lobbyId}")
    Optional<Lobby> getLobby(@PathVariable String lobbyId);
    @PostMapping("/lobby/leave")
    void leaveLobby(@RequestBody String userId);
    @PostMapping("/lobby/{lobbyId}/start")
    void startLobby(@PathVariable String lobbyId);
}
