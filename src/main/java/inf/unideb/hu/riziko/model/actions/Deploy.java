package inf.unideb.hu.riziko.model.actions;

import inf.unideb.hu.riziko.model.map.Territory;

public class Deploy {
    Territory territory;
    int Amount=0;
    public Deploy(Territory territory,int Amount)
    {
        this.territory=territory;
        this.Amount=Amount;
        ResolveDeploy();
    }
    private void ResolveDeploy()
    {
        //todo valahonnan elkéne venni a unitjainkból amit lerakunk de nem találom hol van a unitpool
        territory.AddUnits(Amount);
    }
}
