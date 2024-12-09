package inf.unideb.hu.riziko.model.actions;

import inf.unideb.hu.riziko.model.GameBoard;
import inf.unideb.hu.riziko.model.map.Territory;

public class Fortify {
    Territory Movefrom;
    Territory Moveto;
    int Amount=0;
    public Fortify(Territory Movefrom,Territory Moveto,int Amount)
    {
        this.Movefrom = Movefrom;
        this.Moveto = Moveto;
        this.Amount=Amount;
        ResolveFortify();
    }
    private void ResolveFortify()
    {
        if(Movefrom.removeUnits(Amount))
        {
            Moveto.AddUnits(Amount);
        }
    }
}
