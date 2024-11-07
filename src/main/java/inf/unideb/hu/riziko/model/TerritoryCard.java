package inf.unideb.hu.riziko.model;

public class TerritoryCard {
    enum CardDesign {
        INFANTRY,
        CAVALRY,
        ARTILLERY
    }
    CardDesign Design;
    Integer TerritoryID;
}
