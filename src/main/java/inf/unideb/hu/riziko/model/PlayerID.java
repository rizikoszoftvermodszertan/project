package inf.unideb.hu.riziko.model;

/**
  * A játék résztvevői. Maximum 6-an játszhatják a Rizikót.
  * 2 játékos esetén van egy "semleges" játékos, akit senki nem irányít ill. nem támad, de van területe és egysége.
 *  A játékosok sorrendje determinisztikus, de a kezdőjátékos nem.
 **/

public enum PlayerID {
    NEUTRAL (0),
    PLAYER1 (1),
    PLAYER2 (2),
    PLAYER3 (3),
    PLAYER4 (4),
    PLAYER5 (5),
    PLAYER6 (6);

    private final Integer value;
    PlayerID(Integer value) {
        this.value = value;
    }
    private Integer value() {
        return value;
    }
}
