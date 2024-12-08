package inf.unideb.hu.riziko.model;

import inf.unideb.hu.riziko.model.loader.MapLoader;
import lombok.Getter;

import java.util.ArrayList;

public class GameInstance {
    public enum GamePhase {
        SETUP,
        GAMEPLAY,
        FINISHED
    }
    @Getter
    private GamePhase gamePhase;
    @Getter
    private final GameMode gameMode;
    @Getter
    private final GameBoard gameBoard;
    @Getter
    private ArrayList<Player> players;
    @Getter
    private Turn currentTurn;
    @Getter
    private ArrayList<TerritoryCard> territoryCardDeck;

    public Integer getPlayerCount() {
        return players.size();
    }

    //őrület
    private void addPlayer() {
        switch (players.size()) {
            case 0: players.add(new Player(PlayerID.PLAYER1));
            case 1: players.add(new Player(PlayerID.PLAYER2));
            case 2: players.add(new Player(PlayerID.PLAYER3));
            case 3: players.add(new Player(PlayerID.PLAYER4));
            case 4: players.add(new Player(PlayerID.PLAYER5));
            case 5: players.add(new Player(PlayerID.PLAYER6));
        }
    };

    public GameInstance(GameMode gameMode, Integer playerCount, String mapName) {
        gamePhase = GamePhase.SETUP;
        this.gameMode = gameMode;

        MapLoader loader = new MapLoader(mapName);
        gameBoard = loader.loadMap();
        territoryCardDeck = loader.loadDeck();

        players = new ArrayList<Player>();
        for (int i = playerCount; i > 0; i--) {
            addPlayer();
        }

        currentTurn = new Turn(PlayerID.PLAYER1);
    }
}
