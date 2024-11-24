package inf.unideb.hu.riziko.model;

import ch.qos.logback.core.joran.sanity.Pair;
import inf.unideb.hu.riziko.model.map.Continent;
import inf.unideb.hu.riziko.model.map.Territory;

import java.util.HashMap;
import java.util.HashSet;

/**
 * A játéktábla reprezentálja a játékmezővel kapcsolatos információkat.
 * Territories: A területek listája.
 * Continents: A kontinensek listája.
 * Adjacencies: A szomszédos területeket páronként listázza, a Territories indexe szerint.
 */
public class GameBoard {
    HashMap<String, Territory> territories;
    HashSet<Continent> continents;
    HashSet<Pair<Integer,Integer>> adjacencies;

    public Territory findTerritoryByName(String name) {
        return territories.get(name);
    }

    /**
     * Visszaadja a játékost, aki a kontinens összes területét birtokolja.
     * @return a játékost, aki az összes területet irányítja. Ha nincs ilyen, a NEUTRAL-t.
     */

    public PlayerID getFullController(Continent continent) {
        return continent.getTerritories()
                .stream()
                .map(this::findTerritoryByName)
                .map(Territory::getOwner)
                .reduce(PlayerID.NEUTRAL, (a, b) -> a.equals(b) ? a : PlayerID.NEUTRAL);
    }

    /**
     * Frissíti a megadott kontinens tulajdonosát.
     * @param continent az adott kontinens
     */
    public void updateContinentOwner(Continent continent) {
        continent.setOwner(getFullController(continent));
    }


}
