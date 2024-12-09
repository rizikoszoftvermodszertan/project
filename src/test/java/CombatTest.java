import inf.unideb.hu.riziko.model.actions.Combat;
import inf.unideb.hu.riziko.model.map.Territory;
import org.junit.jupiter.api.Test;

public class CombatTest {
    @Test
    public void combatTest() {
        Territory t1 = new Territory();
        t1.addUnits(0);
        Territory t2 = new Territory();
        t2.addUnits(3);
        Combat combat = new Combat(t1, t2);
        combat.resolveCombat();

    }
}
