package inf.unideb.hu.riziko.model;

import lombok.Getter;
import lombok.ToString;

import java.util.logging.Logger;

/**
 * Egy kör. Egy játékoshoz tartozik és három fázisa van.
 * DEPLOYMENT: "bevetés", a játékos elhelyezi az új seregeit az általa birtokolt területeken.
 * ATTACK: A játékos szomszédos területeket támadhat.
 * FORTIFY: A játékos saját területein belül mozgathat hadseregeket.
 * Léptetés: Az advanceTurnState() függvény a kör állapotát és az aktív játékost is képes változtatni.
 */
@ToString
public class Turn {
    private enum TurnState {
        DEPLOYMENT,
        ATTACK,
        FORTIFY
    }
    @Getter
    TurnState currentState;
    @Getter
    PlayerID activePlayer;

    Turn(PlayerID activePlayer) {
        this.activePlayer = activePlayer;
        this.currentState = TurnState.DEPLOYMENT;
    }

    public void advanceTurnState() {
        switch(this.currentState) {
            case DEPLOYMENT -> this.currentState = TurnState.ATTACK;
            case ATTACK -> this.currentState = TurnState.FORTIFY;
            case FORTIFY -> advancePlayer();
        }
    }

    public void resetActivePlayer() {
        this.activePlayer = PlayerID.PLAYER1;
    }

    public void advancePlayer() {
        this.currentState = TurnState.DEPLOYMENT;
        switch (this.activePlayer) {
            case PLAYER1 -> this.activePlayer = PlayerID.PLAYER2;
            case PLAYER2 -> this.activePlayer = PlayerID.PLAYER3;
            case PLAYER3 -> this.activePlayer = PlayerID.PLAYER4;
            case PLAYER4 -> this.activePlayer = PlayerID.PLAYER5;
            case PLAYER5 -> this.activePlayer = PlayerID.PLAYER6;
            case PLAYER6 -> this.activePlayer = PlayerID.PLAYER1;
            case NEUTRAL -> throw new IllegalStateException("NEUTRAL nem lehet aktív játékos!");
        }
    }
}
