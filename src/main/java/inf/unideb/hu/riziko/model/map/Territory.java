package inf.unideb.hu.riziko.model.map;

import inf.unideb.hu.riziko.model.PlayerID;
import java.util.ArrayList;

/**
 * A játéktábla legkisebb eleme. Minden területnek van egy tulajdonosa és a tulajdonos által állomásozott hadseregek.
 */
public class Territory {
    String Name;
    PlayerID Owner;
    Integer ArmyCount;
}
