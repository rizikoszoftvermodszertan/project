package inf.unideb.hu.riziko.model.map;

import inf.unideb.hu.riziko.model.PlayerID;
import lombok.*;
import org.springframework.lang.NonNull;

import java.util.ArrayList;

/**
 * A játéktábla legkisebb eleme. Minden területnek van egy tulajdonosa és a tulajdonos által állomásozott hadseregek.
 */
@ToString
public class Territory {
    @Getter
    @NonNull
    final Integer ID;
    @Getter
    @NonNull
    final String name;
    @Getter
    @Setter
    @NonNull
    PlayerID owner;
    @Getter
    @Setter
    @NonNull
    Integer armyCount;
    public Territory(Integer ID, String name) {
        this.ID = ID;
        this.name = name;
        this.owner = PlayerID.NEUTRAL;
        this.armyCount = 0;
    }
}
