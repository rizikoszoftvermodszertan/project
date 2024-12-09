package inf.unideb.hu.riziko.model.map;

import inf.unideb.hu.riziko.model.Player;
import inf.unideb.hu.riziko.model.PlayerID;
import lombok.*;
import org.springframework.lang.NonNull;

/**
 * A játéktábla legkisebb eleme. Minden területnek van egy tulajdonosa és a tulajdonos által állomásozott hadseregek.
 */
@ToString
public class Territory {
    @Getter
    @Setter
    @NonNull
    private PlayerID owner;
    @Getter
    @Setter
    @NonNull
    private Integer armyCount;
    public Territory() {
        this.owner = PlayerID.NEUTRAL;
        this.armyCount = 0;
    }

    public void setProperties(PlayerID owner, Integer armyCount) {
        this.owner = owner;
        this.armyCount = armyCount;
    }

    public void removeUnits(int count) {
        this.armyCount -= count;
        if (this.armyCount < 0) this.armyCount = 0;
    }

    public void addUnits(int count) {
        this.armyCount += count;
    }

    public int getUnitCount() {
        return this.armyCount;
    }
}
