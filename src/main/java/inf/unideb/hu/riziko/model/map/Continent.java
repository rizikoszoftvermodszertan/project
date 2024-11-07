package inf.unideb.hu.riziko.model.map;
import inf.unideb.hu.riziko.model.PlayerID;
import java.util.ArrayList;

/**
 * Egy kontinens területek csoportja.
 * Ha egy játékos a kontinens összes területét birtokolja, egy előre meghatározott számú sereget kap a kör elején.
 */
public class Continent {
    String Name;
    ArrayList<Integer> TerritoryIDs;
    Integer ArmyBonus;
    PlayerID Owner;
}
