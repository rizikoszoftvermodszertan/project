package inf.unideb.hu.riziko.model;

import java.util.ArrayList;

public class GameInstance {
    public enum GamePhase {
        SETUP,
        GAMEPLAY,
        FINISHED
    }
    GamePhase gamePhase;
    GameMode gameMode;
    GameBoard gameBoard;
    ArrayList<Player> players;
    Turn currentTurn;
}
