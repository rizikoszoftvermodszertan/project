package inf.unideb.hu.riziko.model;

/**
 * Titkos küldetések. A Secret Mission módban minden játékos szerepel eggyel.
 * Ha a játékos elsőnek teljesíti a küldetését, nyer.
 */
public enum Mission {
    CONQUER_EUR_AUS_PLUS_ONE, //Európa, Ausztrália és egy tetszőleges harmadik kontinens meghódítása
    CONQUER_EUR_SA_PLUS_ONE, //Európa, Dél-Amerika és egy tetszóleges harmadik kontinens meghódítása
    CONQUER_NA_AFR, //Észak-Amerika és Afrika meghódítása
    CONQUER_NA_AUS, //Észak-Amerika és Ausztrália meghódítása
    CONQUER_ASIA_SA, //Ázsia és Dél-Amerika meghódítása
    CONQUER_ASIA_AFR, //Ázsia és Afrika meghódítása
    CONQUER_24, //24 tetszőleges terület meghódítása
    CONQUER_18_AND_FORTIFY, //18 tetszőleges terület meghódítása úgy, hogy 2 hadsereg legyen rajtuk
    KILL_PLAYER1, //A playerID által jelölt játékos kiesése a játékból, vagy (ha az adott PlayerID húzta, vagy az adott ID nem játszik) 24 tetszőleges terület meghódítása
    KILL_PLAYER2,
    KILL_PLAYER3,
    KILL_PLAYER4,
    KILL_PLAYER5,
    KILL_PLAYER6
}
