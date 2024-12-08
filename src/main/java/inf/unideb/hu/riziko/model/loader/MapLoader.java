package inf.unideb.hu.riziko.model.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import inf.unideb.hu.riziko.datastructures.UnorderedPair;
import inf.unideb.hu.riziko.model.GameBoard;
import inf.unideb.hu.riziko.model.map.Continent;
import inf.unideb.hu.riziko.model.map.Territory;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A térkép JSON fáljainak betöltéséért felelős osztály.
 */
public class MapLoader {
    private static final Logger loadLogger = LogManager.getLogger(MapLoader.class.getName());
    private final File continentsFile;
    private final File territoriesFile;
    private final File adjacenciesFile;

    private static class ContinentsStructure {
        @Setter
        protected String name;
        @Setter
        protected ArrayList<String> territories;
        @Setter
        protected Integer armyBonus;
    }

    private static class TerritoriesStructure {
        @Setter
        @Getter
        protected String name;
        @Setter
        protected Integer ID;
    }

    private final ObjectMapper mapper = new ObjectMapper();

    public MapLoader(String mapPath) {
        continentsFile = new File(mapPath + "/continents.json");
        territoriesFile = new File(mapPath + "/territories.json");
        adjacenciesFile = new File(mapPath + "/adjacencies.json");
    }

    public HashSet<Continent> loadContinents() {
        try {
            ContinentsStructure[] continentsData = mapper.readValue(continentsFile, ContinentsStructure[].class);
            HashSet<Continent> continentsSet = Arrays.stream(continentsData)
                    .map(x -> new Continent(x.name, new HashSet<>(x.territories), x.armyBonus))
                    .collect(Collectors.toCollection(HashSet::new));
            loadLogger.info("loaded continent info " + continentsFile.getAbsolutePath());
            return continentsSet;
        } catch (IOException e) {
            loadLogger.error("loading level file " + continentsFile.getAbsolutePath() + " failed due to " + e + "!");
            throw new RuntimeException(e);
        }
    }

    public HashMap<String, Territory> loadTerritories() {
        try {
            TerritoriesStructure[] territoriesData = mapper.readValue(territoriesFile, TerritoriesStructure[].class);
            loadLogger.info("loaded territory info " + territoriesFile.getAbsolutePath());
            return new HashMap<>(
                    Arrays.stream(territoriesData)
                            .collect(Collectors.toMap(
                                    TerritoriesStructure::getName,
                                    x -> new Territory())));
        } catch (IOException e) {
            loadLogger.error("loading level file " + territoriesFile.getAbsolutePath() + " failed due to " + e + "!");
            throw new RuntimeException(e);
        }
    }

    public HashSet<UnorderedPair<String>> loadAdjacencies() {
        try {
            //ha nem stringeket kap, a világ felrobban
            ArrayList<String>[] adjacenciesData = mapper.readValue(adjacenciesFile, ArrayList[].class);
            loadLogger.info("loaded adjacency info " + adjacenciesFile.getAbsolutePath());
            return Arrays.stream(adjacenciesData)
                    .map(x -> new UnorderedPair<String>(x.get(0), x.get(1)))
                    .collect(Collectors.toCollection(HashSet::new));
        } catch (IOException e) {
            loadLogger.error("loading level file " + adjacenciesFile.getAbsolutePath() + " failed due to " + e + "!");
            throw new RuntimeException(e);
        }
    }

    public GameBoard loadMap() {
        return new GameBoard(loadTerritories(), loadContinents(), loadAdjacencies());
    }
}