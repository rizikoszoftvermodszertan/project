package inf.unideb.hu.riziko.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class GameInstance {
    public enum GamePhase {
        SETUP,
        GAMEPLAY,
        FINISHED
    }
    @Getter
    private GamePhase gamePhase = GamePhase.SETUP;
    @Getter
    @Setter
    private GameMode gameMode = GameMode.WORLD_DOMINATION;
    @Getter
    private GameBoard gameBoard;
    @Getter
    private ArrayList<Player> players;
    @Getter
    private Turn currentTurn;
}
