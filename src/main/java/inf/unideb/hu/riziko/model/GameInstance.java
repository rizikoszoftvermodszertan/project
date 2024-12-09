package inf.unideb.hu.riziko.model;

import inf.unideb.hu.riziko.model.actions.Combat;
import inf.unideb.hu.riziko.model.loader.MapLoader;
import inf.unideb.hu.riziko.model.map.Territory;
import inf.unideb.hu.riziko.requests.DeployRequest;
import lombok.Getter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GameInstance {
    public enum GamePhase {
        SETUP,
        GAMEPLAY,
        FINISHED
    }
    private final Logger gameLogger = LogManager.getLogger(GameInstance.class);
    @Getter
    private GamePhase gamePhase;
    @Getter
    private final GameMode gameMode;
    @Getter
    private final GameBoard gameBoard;
    @Getter
    private ArrayList<Player> players;
    @Getter
    private Turn currentTurn;
    @Getter
    private CardDeck territoryCardDeck;

    public Integer getPlayerCount() {
        return players.size();
    }

    //őrület
    private void addPlayer() {
        switch (players.size()) {
            case 0: players.add(new Player(PlayerID.PLAYER1));
            case 1: players.add(new Player(PlayerID.PLAYER2));
            case 2: players.add(new Player(PlayerID.PLAYER3));
            case 3: players.add(new Player(PlayerID.PLAYER4));
            case 4: players.add(new Player(PlayerID.PLAYER5));
            case 5: players.add(new Player(PlayerID.PLAYER6));
        }
    }

    public GameInstance(GameMode gameMode, Integer playerCount, String mapName) {
        gamePhase = GamePhase.SETUP;
        this.gameMode = gameMode;

        MapLoader loader = new MapLoader(mapName);
        gameBoard = loader.loadMap();
        territoryCardDeck = new CardDeck(loader.loadDeck());

        players = new ArrayList<Player>();
        for (int i = playerCount; i > 0; i--) {
            addPlayer();
        }
        currentTurn = new Turn(PlayerID.PLAYER1);

        gameBoard.distributeTerritories(players.stream().map(Player::getID).toList());
    }

    public void startGame() {
        if (gamePhase != GamePhase.SETUP) return;
        advanceGamePhase();
        startTurn();
    }

    public void advanceGamePhase() {
        switch (gamePhase) {
            case SETUP -> gamePhase = GamePhase.GAMEPLAY;
            case GAMEPLAY ->  gamePhase = GamePhase.FINISHED;
            case FINISHED -> gameLogger.info("A játék már véget ért, de az advanceGamePhase metódus meghívódott.");
        }
    }

    public void startTurn() {
        if (gamePhase != GamePhase.GAMEPLAY) {
            throw new IllegalStateException("A játék nincs játék fázisban.");
        }

        Player currentPlayer = getCurrentPlayer();

        // Új kártya húzása, ha van még a pakliban és a játékos megteheti
        if (territoryCardDeck.hasCards() && currentPlayer.hasTakenTerritory()) {
            TerritoryCard newCard = territoryCardDeck.drawCard();
            currentPlayer.addCard(newCard);
            gameLogger.info(currentPlayer.getID() + " új kártyája: " + newCard.toString());
        } else {
            gameLogger.info("Húzni próbált " + currentPlayer.getID() + " de a pakli üres");
        }

        // Kártyák beváltásának megkísérlése
        try {
            List<TerritoryCard> cardsToRedeem = currentPlayer.checkForRedeemableCards();
            if (!cardsToRedeem.isEmpty()) {
                int bonusUnits = currentPlayer.redeemCards(cardsToRedeem);
                System.out.println("Beváltott egységek száma: " + bonusUnits);
            } else {
                System.out.println("Nincs beváltható kártyakombináció.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private Player getCurrentPlayer() {
        return players.stream()
                .filter(player -> player.getID() == currentTurn.getActivePlayer())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Nincs aktív játékos a körben."));
    }

    /**
     * Elindít egy harcot két terület között.
     * @param attacker támadó terület (NEM JÁTÉKOS) neve.
     * @param defender védő terület (NEM JÁTÉKOS) neve.
     */
    public void attack(String attacker, String defender) {
        if (currentTurn.currentState != Turn.TurnState.ATTACK) return;
        if (gameBoard.findTerritoryByName(attacker).getOwner() == gameBoard.findTerritoryByName(defender).getOwner()){
            gameLogger.error(attacker + " és " + defender + " területet ugyanaz irányítja!");
            return;
        }
        if (!gameBoard.isAdjacent(attacker, defender)) {
            gameLogger.error(attacker + " és " + defender + " nem szomszédosak!");
            return;
        }
        Combat combat = new Combat(gameBoard.findTerritoryByName(attacker), gameBoard.findTerritoryByName(defender));
        combat.resolveCombat();
        gameBoard.updateTerritory(attacker, combat.getAttackingTerritory());
        gameBoard.updateTerritory(defender, combat.getDefendingTerritory());
    }

    public void fortify(String origin, String destination, Integer armyCount) {
        if (currentTurn.currentState != Turn.TurnState.FORTIFY) return;
        if (gameBoard.findTerritoryByName(origin).getOwner() != gameBoard.findTerritoryByName(destination).getOwner()){
            gameLogger.error(origin + " és " + destination + " területet nem ugyanaz irányítja!");
            return;
        }
        if (!gameBoard.isAdjacent(origin, destination)) {
            gameLogger.error(origin + " és " + destination + " nem szomszédosak!");
            return;
        }
        if (armyCount - 1 > gameBoard.findTerritoryByName(origin).getArmyCount()) {
            gameLogger.error("Legalább egy egységnek maradnia kell " + origin + " területen!");
            return;
        }
        gameBoard.findTerritoryByName(origin).removeUnits(armyCount);
        gameBoard.findTerritoryByName(destination).addUnits(armyCount);
    }

    public void deploy(List<DeployRequest.Deployment> deployments) {
        deployments.forEach((deployment) -> {
            String to = deployment.getDeploy();
            int amount = deployment.getAmount();
            this.getGameBoard().getTerritories().get(to).addUnits(amount);
        });
        currentTurn.advanceTurnState();
    }

    private void concludeTurn() {
        if (checkIfGameOver()) {
            gamePhase = GamePhase.FINISHED;
        }

        currentTurn.advancePlayer();
        if (currentTurn.getActivePlayer().value() > getPlayerCount()) {
            currentTurn.resetActivePlayer();
        }
    }

    /**
     * Ellenőrzi, hogy véget ért-e a játék.
     */
    private boolean checkIfGameOver() {
        //TODO: más gamemode-ok???
        return gameBoard.getTerritories().values().stream()
                .map(Territory::getOwner)
                .distinct()
                .filter(x -> x!= PlayerID.NEUTRAL)
                .toList().size() == 1;
    }

    private void calculatePlayerIncomes() {
        for(var p : players) {
            p.setArmyIncome(
                    gameBoard.getTerritories().keySet().size() / 3
                            + (int) gameBoard.getContinents().stream().filter(c -> c.getOwner() == p.getID()).count());
        }
    }

    public static class CardDeck {
        private List<TerritoryCard> cards;

        public CardDeck(List<TerritoryCard> initialCards) {
            if (initialCards == null || initialCards.isEmpty()) {
                throw new IllegalArgumentException("Nem lehet üres a kártyapakli.");
            }
            this.cards = new ArrayList<>(initialCards);
            java.util.Collections.shuffle(this.cards);
        }

        public boolean hasCards() {
            return !cards.isEmpty();
        }

        public TerritoryCard drawCard() {
            if (cards.isEmpty()) {
                throw new IllegalStateException("A pakli üres.");
            }
            return cards.remove(cards.size() - 1);
        }
    }

    public Player getPlayerByID(PlayerID id)
    {
        for (Player p : players)
        {
            if(p.getID().equals(id))
            {
                return p;
            }
        }

    return null;
    }
}