package inf.unideb.hu.riziko.model.actions;

import inf.unideb.hu.riziko.model.Player;
import inf.unideb.hu.riziko.model.map.Territory;

public class Deploy {
    Territory territory;
    int Amount=0;
    Player player;
    public Deploy(Territory territory,int Amount, Player player)
    {
        this.player=player;
        this.territory=territory;
        this.Amount=Amount;
        ResolveDeploy();
    }
    private void ResolveDeploy()
    {
        if (player.getArmyIncome()!=0 && player.getArmyIncome()>=Amount)
        {
            player.IncreaseArmySize(-Amount);
            territory.AddUnits(Amount);
        }

    }
}
