package inf.unideb.hu.riziko.model;

import inf.unideb.hu.riziko.model.loader.MapLoader;
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

        // Új kártya húzása, ha van még a pakliban
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

        currentTurn.advanceTurnState();
        if (currentTurn.getActivePlayer() == players.get(0).getID()) {
            concludeTurn();
        }
    }

    private Player getCurrentPlayer() {
        return players.stream()
                .filter(player -> player.getID() == currentTurn.getActivePlayer())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Nincs aktív játékos a körben."));
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

    private boolean checkIfGameOver() {
        // Implementáció eldönti, hogy a játék véget ért-e
        return false;
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
}