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

    public GameInstance(GameMode gameMode, Integer playerCount, String mapName) {
        gamePhase = GamePhase.SETUP;
        this.gameMode = gameMode;

        MapLoader loader = new MapLoader(mapName);
        gameBoard = loader.loadMap();
        territoryCardDeck = loader.loadDeck();

    }
}
