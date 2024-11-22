package inf.unideb.hu.riziko.model;

/**
  * A játék résztvevői. Maximum 6-an játszhatják a Rizikót.
  * 2 játékos esetén van egy "semleges" játékos, akit senki nem irányít ill. nem támad, de van területe és egysége.
 *  A játékosok sorrendje determinisztikus, de a kezdőjátékos nem.
 **/

public enum PlayerID {
    NEUTRAL,
    PLAYER1,
    PLAYER2,
    PLAYER3,
    PLAYER4,
    PLAYER5,
    PLAYER6
}
