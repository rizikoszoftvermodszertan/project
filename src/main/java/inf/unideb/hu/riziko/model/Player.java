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
    PlayerID ID;
    @Getter
    Territory HQLocation; //Csak a Capital játékmódban!
    @Getter
    Mission mission; //Csak a Secret Mission játékmódban!
    ArrayList<TerritoryCard> cards;
}
