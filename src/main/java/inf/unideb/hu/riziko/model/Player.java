package inf.unideb.hu.riziko.model;

import inf.unideb.hu.riziko.model.map.Territory;
import inf.unideb.hu.riziko.model.TerritoryCard.CardDesign;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Játékos. Rendelkezik ID-val, játékmód szerint titkos küldetéssel vagy főhadiszállással, illetve kártyákkal.
 */
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Player {
    @Getter
    @NonNull
    @EqualsAndHashCode.Include
    private final PlayerID ID;
    @Getter
    private final Territory HQLocation; //Csak a Capital játékmódban!
    @Getter
    private final Mission mission; //Csak a Secret Mission játékmódban!
    @Getter
    private ArrayList<TerritoryCard> cards;
    @Setter
    private boolean takenTerritory = false;
    @Getter
    @Setter
    private Integer armyIncome = 0;

    public boolean hasTakenTerritory() {
        return takenTerritory;
    }

    public Player(@NonNull PlayerID ID) {
        this.ID = ID;
        this.HQLocation = null;
        this.mission = null;
        this.cards = new ArrayList<>();
    }


    public void addCard(TerritoryCard territoryCard) {
        cards.add(territoryCard);
    }

    public int redeemCards(List<TerritoryCard> cardsToRedeem) {
        int unitBonus = 0;

        boolean validSet = checkValidSet(cardsToRedeem);
        if (validSet) {
            unitBonus = getUnitBonus(cardsToRedeem);
            this.cards.removeAll(cardsToRedeem);
        } else {
            throw new IllegalArgumentException("Érvénytelen kártyakombináció");
        }

        return unitBonus;
    }

    private boolean checkValidSet(List<TerritoryCard> cardsToRedeem) {
        if (cardsToRedeem.size() != 3) return false;

        boolean allSame = cardsToRedeem.stream().allMatch(c -> c.getDesign() == cardsToRedeem.get(0).getDesign());
        boolean allDifferent = cardsToRedeem.stream().map(TerritoryCard::getDesign).distinct().count() == 3;

        return allSame || allDifferent;
    }

    private int getUnitBonus(List<TerritoryCard> cardsToRedeem) {
        int bonus = 0;
        switch (cardsToRedeem.get(0).getDesign()) {
            case INFANTRY -> bonus = 4;
            case CAVALRY -> bonus = 6;
            case ARTILLERY -> bonus = 8;
            case WILD -> bonus = 10;
        }
        return bonus;
    }
    public List<TerritoryCard> checkForRedeemableCards() {
        if (cards.size() < 3) {
            throw new IllegalArgumentException("Nincs elegendő kártya a beváltáshoz.");
        }

        List<TerritoryCard> validSet = findRedeemableSet();
        if (validSet.isEmpty()) {
            throw new IllegalArgumentException("Nincs beváltható kártyakombináció.");
        }
        return validSet;
    }

    private List<TerritoryCard> findRedeemableSet() {
        // Ellenőrizzük az összes lehetséges 3-as kártyakombinációt.
        for (int i = 0; i < cards.size() - 2; i++) {
            for (int j = i + 1; j < cards.size() - 1; j++) {
                for (int k = j + 1; k < cards.size(); k++) {
                    List<TerritoryCard> potentialSet = List.of(cards.get(i), cards.get(j), cards.get(k));

                    if (checkValidSet(potentialSet)) {
                        return potentialSet;
                    }
                }
            }
        }
        return new ArrayList<>();  // Ha nincs érvényes szett
    }

    public void IncreaseArmySize(int amount)
    {
        armyIncome += amount;
    }
}
