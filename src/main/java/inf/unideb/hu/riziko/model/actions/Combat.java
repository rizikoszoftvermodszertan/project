package inf.unideb.hu.riziko.model.actions;

import inf.unideb.hu.riziko.model.map.Territory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Combat {
    private Territory attackingTerritory;
    private Territory defendingTerritory;

    public Combat(Territory attackingTerritory, Territory defendingTerritory) {
        this.attackingTerritory = attackingTerritory;
        this.defendingTerritory = defendingTerritory;
        resolveCombat();
    }

    public void resolveCombat() {

        int attackerDiceCount = Math.min(3, attackingTerritory.getUnitCount() - 1);
        int defenderDiceCount = Math.min(2, defendingTerritory.getUnitCount());


        List<Integer> attackerRolls = rollDice(attackerDiceCount);
        List<Integer> defenderRolls = rollDice(defenderDiceCount);


        Collections.sort(attackerRolls, Collections.reverseOrder());
        Collections.sort(defenderRolls, Collections.reverseOrder());


        int rounds = Math.min(attackerRolls.size(), defenderRolls.size());
        for (int i = 0; i < rounds; i++) {
            if (attackerRolls.get(i) > defenderRolls.get(i)) {
                System.out.println("Defender loses 1 unit.");
                defendingTerritory.removeUnits(1);
            } else {
                System.out.println("Attacker loses 1 unit.");
                attackingTerritory.removeUnits(1);
            }
        }


        if (defendingTerritory.getUnitCount() <= 0) {
            System.out.println("Defending territory conquered!");
            defendingTerritory.setOwner(attackingTerritory.getOwner());
            defendingTerritory.setArmyCount(attackerDiceCount);

            attackingTerritory.removeUnits(attackerDiceCount);

        }
    }

    private List<Integer> rollDice(int count) {
        Random rand = new Random();
        List<Integer> rolls = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            rolls.add(rand.nextInt(6) + 1);
        }
        System.out.println("Dice rolls: " + rolls);
        return rolls;
    }
}
