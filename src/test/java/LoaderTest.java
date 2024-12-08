import inf.unideb.hu.riziko.model.GameBoard;
import inf.unideb.hu.riziko.model.loader.MapLoader;
import inf.unideb.hu.riziko.model.map.Continent;
import inf.unideb.hu.riziko.model.map.Territory;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertSame;

public class LoaderTest {
    @Test
    public void continentLoadTest() {
        MapLoader mapLoader = new MapLoader("src/main/resources/data/maps/world");
        HashSet<Continent> continents = mapLoader.loadContinents();
        System.out.println(continents);
    }

    @Test
    public void territoryLoadTest() {
        MapLoader mapLoader = new MapLoader("src/main/resources/data/maps/world");
        var territories = mapLoader.loadTerritories();
        System.out.println(territories);
    }

    @Test
    public void adjacencyLoadTest() {
        MapLoader mapLoader = new MapLoader("src/main/resources/data/maps/world");
        var adjacencies = mapLoader.loadAdjacencies();
        System.out.println(adjacencies);
    }
}
