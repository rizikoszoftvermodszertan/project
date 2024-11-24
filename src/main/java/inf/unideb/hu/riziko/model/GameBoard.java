package inf.unideb.hu.riziko.model;

import inf.unideb.hu.riziko.datastructures.UnorderedPair;
import inf.unideb.hu.riziko.model.map.Continent;
import inf.unideb.hu.riziko.model.map.Territory;
import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;

/**
 * A játéktábla reprezentálja a játékmezővel kapcsolatos információkat.
 * Territories: A területek listája.
 * Continents: A kontinensek listája.
 * Adjacencies: A szomszédos területeket páronként listázza, a Territories String kulcsok szerint.
 */

public class GameBoard {

    @Getter
    private final HashMap<String, Territory> territories;
    @Getter
    private final HashSet<Continent> continents;
    @Getter
    private final HashSet<UnorderedPair<String>> adjacencies;

    public GameBoard(HashMap<String, Territory> territories, HashSet<Continent> continents, HashSet<UnorderedPair<String>> adjacencies) {
        this.territories = territories;
        this.continents = continents;
        this.adjacencies = adjacencies;
    }

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
     * Frissíti a megadott kontinens tulajdonosát (ha egy játékos az egész kontinenst uralja, tulajdonos lesz, és ha nem, elveszti a tulajdonosi rangját).
     * @param continent az adott kontinens
     */
    public void updateContinentOwner(Continent continent) {
        continent.setOwner(getFullController(continent));
    }

    public boolean isAdjacent(String territory1, String territory2) {
        return adjacencies.contains(new UnorderedPair<String>(territory1,territory2));
    }
}
