package inf.unideb.hu.riziko.model;

import ch.qos.logback.core.joran.sanity.Pair;
import inf.unideb.hu.riziko.model.map.Continent;
import inf.unideb.hu.riziko.model.map.Territory;

import java.util.ArrayList;

public class GameBoard {
    ArrayList<Territory> Territories;
    ArrayList<Continent> Continents;
    ArrayList<Pair<Integer,Integer>> Adjacencies;
}
