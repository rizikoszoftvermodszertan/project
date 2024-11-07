package inf.unideb.hu.riziko.model;

import inf.unideb.hu.riziko.model.map.Territory;

import java.util.ArrayList;

public class Player {
    PlayerID ID;
    Territory HQLocation; //Csak a Capital játékmódban!
    Mission Mission; //Csak a Secret Mission játékmódban!
    ArrayList<TerritoryCard> Cards;
}
