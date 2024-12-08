package inf.unideb.hu.riziko.model;

import inf.unideb.hu.riziko.model.map.Territory;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.ArrayList;

/**
 * Játékos. Rendelkezik ID-val, játékmód szerint titkos küldetéssel vagy főhadiszállással, illetve kártyákkal.
 */
@ToString
public class Player {
    @Getter
    @NonNull
    private final PlayerID ID;
    @Getter
    private Territory HQLocation; //Csak a Capital játékmódban!
    @Getter
    private final Mission mission; //Csak a Secret Mission játékmódban!
    @Getter
    private ArrayList<TerritoryCard> cards;

    public Player(@NonNull PlayerID ID) {
        this.ID = ID;
        this.HQLocation = null;
        this.mission = null;
        this.cards = new ArrayList<>();
    }

    public void addCard(TerritoryCard territoryCard) {
        cards.add(territoryCard);
    }
}
