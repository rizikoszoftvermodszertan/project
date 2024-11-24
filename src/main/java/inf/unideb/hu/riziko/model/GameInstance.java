package inf.unideb.hu.riziko.model;

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
    private GameMode gameMode;
    @Getter
    private GameBoard gameBoard;
    @Getter
    private ArrayList<Player> players;
    @Getter
    private Turn currentTurn;
}
