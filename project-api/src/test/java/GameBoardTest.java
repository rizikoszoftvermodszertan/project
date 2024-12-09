import inf.unideb.hu.riziko.model.GameBoard;
import inf.unideb.hu.riziko.model.PlayerID;
import inf.unideb.hu.riziko.model.TerritoryCard;
import inf.unideb.hu.riziko.model.loader.MapLoader;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class GameBoardTest {
    @Test
    public void distributeTerrTest() {
        MapLoader mapLoader = new MapLoader("src/main/resources/data/maps/world");
        GameBoard testBoard = mapLoader.loadMap();
        ArrayList<PlayerID> players = new ArrayList<>();
        players.add(PlayerID.PLAYER1);
        players.add(PlayerID.PLAYER2);
        players.add(PlayerID.PLAYER3);
        players.add(PlayerID.PLAYER4);
        players.add(PlayerID.PLAYER5);

        testBoard.distributeTerritories(players);
        System.out.println(testBoard);
    }
}
