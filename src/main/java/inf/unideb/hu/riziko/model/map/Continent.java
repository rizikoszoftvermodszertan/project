package inf.unideb.hu.riziko.model.map;
import inf.unideb.hu.riziko.model.PlayerID;
import lombok.NonNull;

import java.util.ArrayList;

/**
 * Egy kontinens területek csoportja.
 * Ha egy játékos a kontinens összes területét birtokolja, egy előre meghatározott számú sereget kap a kör elején.
 */
public class Continent {
    @NonNull
    String name;
    @NonNull
    ArrayList<Integer> territoryIDs;
    @NonNull
    Integer armyBonus;
    @NonNull
    PlayerID owner;
}
