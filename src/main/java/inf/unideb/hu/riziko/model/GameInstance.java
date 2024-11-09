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
    GamePhase gamePhase;
    @Getter
    GameMode gameMode;
    @Getter
    GameBoard gameBoard;
    @Getter
    ArrayList<Player> players;
    @Getter
    Turn currentTurn;
}
