package inf.unideb.hu.riziko.model;

import ch.qos.logback.core.joran.sanity.Pair;
import inf.unideb.hu.riziko.model.map.Continent;
import inf.unideb.hu.riziko.model.map.Territory;

import java.util.ArrayList;

/**
 * A játéktábla reprezentálja a játékmezővel kapcsolatos információkat.
 * Territories: A területek listája.
 * Continents: A kontinensek listája.
 * Adjacencies: A szomszédos területeket páronként listázza, a Territories indexe szerint.
 */
public class GameBoard {
    ArrayList<Territory> territories;
    ArrayList<Continent> continents;
    ArrayList<Pair<Integer,Integer>> adjacencies;
}
