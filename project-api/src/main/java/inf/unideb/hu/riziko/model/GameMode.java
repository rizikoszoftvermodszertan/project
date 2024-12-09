package inf.unideb.hu.riziko.model;

/**
 * A szabályok által megadott 3 játékmód.

 * World Domination: A játék akkor ér véget, amikor egy játékos az egész térképet meghódítja.
 * A játékosok választják a kezdő területeiket.

 * Capital: A játék elején mindenki választ egy Főhadiszállást (Headquarters).
 * A játék akkor ér véget, ha egy játékos minden játékos főhadiszállását birtokolja.

 * Secret Mission: Minimum 3 játékos. A területek véletlenszerűen vannak kiosztva.
 * A játék elején minden játékos kap egy küldetéskártyát (Mission Card), amit a többiektől elrejt.
 * A győztes az, aki a először teljesíti a kártyáján található feladatot.
 */
public enum GameMode {
    WORLD_DOMINATION,
    CAPITAL,
    SECRET_MISSION
}
