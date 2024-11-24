package inf.unideb.hu.riziko.model.map;
import inf.unideb.hu.riziko.model.PlayerID;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Egy kontinens területek csoportja.
 * Ha egy játékos a kontinens összes területét birtokolja, egy előre meghatározott számú sereget kap a kör elején.
 *
 * owner: A tulajdonos. NEUTRAL = nincs tulajdonos.
 */
public class Continent {
    @Getter
    @NonNull
    private final String name;
    @NonNull
    @Getter
    private final Set<String> territories;
    @Getter
    @NonNull
    private final Integer armyBonus;
    @Getter
    @Setter
    @NonNull
    private PlayerID owner;

    public Continent(String name, ArrayList<String> territories, Integer armyBonus) {
        this.name = name;
        this.armyBonus = armyBonus;
        this.territories = new HashSet<>(territories);
        owner = PlayerID.NEUTRAL;
    }
}
