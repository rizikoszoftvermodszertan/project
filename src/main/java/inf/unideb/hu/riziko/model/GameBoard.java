package inf.unideb.hu.riziko.model;

import inf.unideb.hu.riziko.datastructures.UnorderedPair;
import inf.unideb.hu.riziko.model.map.Continent;
import inf.unideb.hu.riziko.model.map.Territory;
import jdk.dynalink.Operation;
import lombok.Getter;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A játéktábla reprezentálja a játékmezővel kapcsolatos információkat.
 * Territories: A területek listája.
 * Continents: A kontinensek listája.
 * Adjacencies: A szomszédos területeket páronként listázza, a Territories String kulcsok szerint.
 */

@ToString
public class GameBoard {
    private final Logger logger = LogManager.getLogger(GameBoard.class.getName());
    @Getter
    private final HashMap<String, Territory> territories;
    @Getter
    private final HashSet<Continent> continents;
    @Getter
    private final HashSet<UnorderedPair<String>> adjacencies;

    public GameBoard(HashMap<String, Territory> territories,  HashSet<Continent> continents, HashSet<UnorderedPair<String>> adjacencies) {
        this.territories = territories;
        this.continents = continents;
        this.adjacencies = adjacencies;
    }

    public Territory findTerritoryByName(String name) {
        return territories.get(name);
    }

    public Continent findContinentByTerritoryName(String territoryName) {
        try {
            if (territoryName == null) return null;
            return continents.stream()
                    .filter(continent -> continent.getTerritories().contains(territoryName))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Territory name " + territoryName + " not found in any continent!"));
        } catch (Exception e) {
            logger.error("Error finding continent by territory name: ", e);
            throw e;
        }
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

    /**
     * Megváltoztatja a tartomány tulajdonosát, úgy, hogy a kontinens tulajdonát ellenőrzi.
     * @param province Az elfoglalni kívánt tartomány neve
     * @param player Az elfoglaló játékos
     */
    public void changeProvinceOwner(String province, PlayerID player) {
        try {
            territories.get(province).setOwner(player);
            updateContinentOwner(findContinentByTerritoryName(province));
        }
        catch (Exception e) {
            logger.error(e.toString());
        }
    }

    public void distributeTerritories(List<PlayerID> players) {
        Integer[] armyCount = {40, 35, 30, 25, 20};

        if (players.size() < 2 || players.size() > 6) throw new IllegalArgumentException("Túl kevés vagy túl sok PlayerID megadva");
        if (players.size() == 2) players.add(PlayerID.NEUTRAL);

        //nyomon tartja, hogy kinél mi van
        HashMap<PlayerID, ArrayList<String>> territoryByPlayer = new HashMap<>();
        for (var i : players) territoryByPlayer.put(i, new ArrayList<String>());

        List<String> territoriesQueue = new ArrayList<>(territories.keySet());
        Collections.shuffle(territoriesQueue);

        //elosztja a területeket
        while (!territoriesQueue.isEmpty()) {
            String currentTerr = territoriesQueue.removeFirst();
            PlayerID currentPlayer = players.getFirst();

            changeProvinceOwner(currentTerr, currentPlayer);
            territoryByPlayer.get(currentPlayer).add(currentTerr);

            players.add(players.removeFirst());
        }

        //a neutral területeknek ad 1 egységet
        try {
            territoryByPlayer.get(PlayerID.NEUTRAL).stream()
                    .map(this::findTerritoryByName)
                    .forEach(x -> {x.addUnits(1);});
        }
        catch (NullPointerException ignored) {} // ha nincs, nem csinál semmit

        for (var p: players) {
            var unitDivisions = divideRandomly(armyCount[players.size()-1], territoryByPlayer.get(p).size());
            territoryByPlayer.get(p).stream()
                    .map(this::findTerritoryByName)
                    .forEach(x -> {x.addUnits(unitDivisions.removeFirst());});
        }
    }

    /**
     * Segítőfüggvény. Szétosz egy számot n részre véletlenszerűen, úgy, hogy mindenhol legyen legalább 1
     * @param number elosztandó szám
     * @param between hányfelé
     * @return Eredmények listája, mindig between hosszú.
     */
    private static ArrayList<Integer> divideRandomly(int number, int between) {
        ArrayList<Double> division = new ArrayList<>();
        double totalWeight = 0;

        //súlyozás
        for (int i = 0; i < between; i++) {
            double weight = 1 - Math.random();
            division.add(weight);
            totalWeight += weight;
        }
        System.out.println(division);

        //denormalizálás
        ArrayList<Integer> division2 = new ArrayList<>();
        int totalAllocated = 0;

        for (int i = 0; i < between; i++) {
            int part = (int) ((division.get(i) / totalWeight) * (number - between));
            division2.add(part + 1);
            totalAllocated += part + 1;
        }

        int remaining = number - totalAllocated;

        //maradék elosztása
        for (int i = 0; i < remaining; i++) {
            int bucket = (int) (Math.random() * between);
            division2.set(bucket, division2.get(bucket) + 1);
        }

        return division2;
    }
}
