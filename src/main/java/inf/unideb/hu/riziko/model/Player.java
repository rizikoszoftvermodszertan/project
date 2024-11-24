package inf.unideb.hu.riziko.model;

import inf.unideb.hu.riziko.model.map.Territory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;

/**
 * Játékos. Rendelkezik ID-val, játékmód szerint titkos küldetéssel vagy főhadiszállással, illetve kártyákkal.
 */
@ToString
public class Player {
    @Getter
    private final PlayerID ID;
    @Getter
    private final Territory HQLocation; //Csak a Capital játékmódban!
    @Getter
    private final Mission mission; //Csak a Secret Mission játékmódban!
    ArrayList<TerritoryCard> cards;

    public Player(PlayerID ID, Territory HQLocation, Mission mission) {
        this.ID = ID;
        this.HQLocation = HQLocation;
        this.mission = mission;
        this.cards = new ArrayList<>();
    }

    public void addCard(TerritoryCard territoryCard) {
        cards.add(territoryCard);
    }
}
