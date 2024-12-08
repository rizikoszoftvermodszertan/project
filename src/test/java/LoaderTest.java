import inf.unideb.hu.riziko.model.GameBoard;
import inf.unideb.hu.riziko.model.TerritoryCard;
import inf.unideb.hu.riziko.model.loader.MapLoader;
import inf.unideb.hu.riziko.model.map.Continent;
import inf.unideb.hu.riziko.model.map.Territory;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertSame;

public class LoaderTest {
    @Test
    public void deckLoadTest() {
        MapLoader mapLoader = new MapLoader("src/main/resources/data/maps/world");
        ArrayList<TerritoryCard> continents = mapLoader.loadDeck();
        System.out.println(continents);
    }
}
