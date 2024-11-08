package inf.unideb.hu.riziko.model;

import inf.unideb.hu.riziko.model.map.Territory;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;

/**
 * Játékos. Rendelkezik ID-val, játékmód szerint titkos küldetéssel vagy főhadiszállással, illetve kártyákkal.
 */
@ToString
@AllArgsConstructor
public class Player {
    PlayerID ID;
    Territory HQLocation; //Csak a Capital játékmódban!
    Mission mission; //Csak a Secret Mission játékmódban!
    ArrayList<TerritoryCard> cards;
}
