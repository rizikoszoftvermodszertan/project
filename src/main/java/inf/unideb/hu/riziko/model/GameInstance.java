package inf.unideb.hu.riziko.model;

import java.util.ArrayList;

public class GameInstance {
    public enum GamePhase {
        SETUP,
        GAMEPLAY,
        FINISHED
    }
    GamePhase GamePhase;
    GameMode GameMode;
    GameBoard GameBoard;
    ArrayList<Player> Players;
    ArrayList<Turn> GameHistory;
    Turn CurrentTurn;
}
