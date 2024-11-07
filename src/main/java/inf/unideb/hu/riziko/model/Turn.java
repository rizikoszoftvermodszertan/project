package inf.unideb.hu.riziko.model;

/**
 * Egy kör. Egy játékoshoz tartozik és három fázisa van.
 * DEPLOYMENT: "bevetés", a játékos elhelyezi az új seregeit az általa birtokolt területeken.
 * ATTACK: A játékos szomszédos területeket támadhat.
 * FORTIFY: A játékos saját területein belül mozgathat hadseregeket.
 */
public class Turn {
    public enum TurnState {
        DEPLOYMENT,
        ATTACK,
        FORTIFY
    }
    PlayerID ActivePlayer;
}
