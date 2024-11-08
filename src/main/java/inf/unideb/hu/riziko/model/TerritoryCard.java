package inf.unideb.hu.riziko.model;

import lombok.ToString;

/**
 * Kártya. Három kártya, amin vagy egyező, vagy mind különböző szimbólum van, beváltható egy meghatározott számú seregre.
 * A WILD típust bármilyen szimbólumként lehet használni.
 * Ha a beváltó játékos birtokol legalább egy területet, ami a beváltott kártyákon szerepel, akkor kap 2 extra sereget.
 */
@ToString
public class TerritoryCard {
    enum CardDesign {
        INFANTRY,
        CAVALRY,
        ARTILLERY,
        WILD
    }
    CardDesign design;
    Integer territoryID; //Ha WILD kártya, akkor ez NULL!!!
}
