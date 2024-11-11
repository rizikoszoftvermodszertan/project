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
    final String name;
    @NonNull
    final ArrayList<Integer> territoryIDs;
    @NonNull
    final Integer armyBonus;
    @NonNull
    PlayerID owner;

    public Continent(String name, ArrayList<Integer> territoryIDs, Integer armyBonus) {
        this.name = name;
        this.territoryIDs = territoryIDs;
        this.armyBonus = armyBonus;
        owner = PlayerID.NEUTRAL;
    }
}
