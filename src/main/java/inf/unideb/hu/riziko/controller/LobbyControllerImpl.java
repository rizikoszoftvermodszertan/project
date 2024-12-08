package inf.unideb.hu.riziko.controller;

import inf.unideb.hu.riziko.model.Lobby.Lobby;
import inf.unideb.hu.riziko.model.Lobby.User;
import inf.unideb.hu.riziko.repository.LobbyRepository;
import inf.unideb.hu.riziko.repository.UserRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class LobbyControllerImpl implements LobbyController{
    LobbyRepository lobbyRepository;
    UserRepository userRepository;

    public LobbyControllerImpl(LobbyRepository lobbyRepository, UserRepository userRepository) {
        this.lobbyRepository = lobbyRepository;
        this.userRepository = userRepository;
    }
    @Override
    public String createLobby(String userId) {
        User user = userRepository.getUser(userId);
        return lobbyRepository.createLobby(user).getLobbyId();
    }

    @Override
    public void joinLobby(String userId, String lobbyId) {
        User user = userRepository.getUser(userId);
        lobbyRepository.joinLobby(user, lobbyId);
    }

    @Override
    public Optional<Lobby> getLobby(String lobbyId) {
        return Optional.of(lobbyRepository.getLobby(lobbyId));
    }

    @Override
    public void leaveLobby(String userId) {
        User user = userRepository.getUser(userId);
        lobbyRepository.leaveLobby(user);
    }

    @Override
    public void startLobby(String lobbyId) {
        lobbyRepository.startGame(lobbyId);
    }

    @Override
    public void setGameMode(String lobbyId, String gameMode) {
        lobbyRepository.setGameMode(lobbyId, gameMode);
    }
}
