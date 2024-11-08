package inf.unideb.hu.riziko.model.map;

import inf.unideb.hu.riziko.model.PlayerID;
import lombok.NonNull;
import lombok.ToString;

import java.util.ArrayList;

/**
 * A játéktábla legkisebb eleme. Minden területnek van egy tulajdonosa és a tulajdonos által állomásozott hadseregek.
 */
@ToString
public class Territory {
    @NonNull
    String name;
    @NonNull
    PlayerID owner;
    @NonNull
    Integer armyCount;
}
