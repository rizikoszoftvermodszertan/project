package inf.unideb.hu.riziko.model;

public class GameInstance {
    public enum GamePhase {
        SETUP,
        GAMEPLAY,
        FINISHED
    }
    GamePhase GamePhase;
    GameMode GameMode;

    GameBoard GameBoard;
    Player[] Players;
}
