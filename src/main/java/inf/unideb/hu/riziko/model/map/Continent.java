package inf.unideb.hu.riziko.model.map;
import inf.unideb.hu.riziko.model.PlayerID;

import java.util.ArrayList;

public class Continent {
    Integer ID;
    String Name;
    ArrayList<Integer> TerritoryIDs;
    Integer ArmyBonus;
    PlayerID Owner;
}
