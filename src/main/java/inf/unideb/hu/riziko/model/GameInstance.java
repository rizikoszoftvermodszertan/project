package inf.unideb.hu.riziko.model;

import java.util.List;
import java.util.ArrayList;

public class GameInstance {
    public enum GamePhase {
        SETUP,
        GAMEPLAY,
        FINISHED
    }

    private GamePhase gamePhase;
    private GameMode gameMode;
    private GameBoard gameBoard;
    private ArrayList<Player> players;
    private Turn currentTurn;
    private CardDeck deck;

    public GameInstance(ArrayList<Player> players, CardDeck deck) {
        if (players == null || players.isEmpty()) {
            throw new IllegalArgumentException("A játékosok listája nem lehet üres.");
        }
        if (deck == null || !deck.hasCards()) {
            throw new IllegalArgumentException("A pakli nem lehet üres.");
        }
        this.players = players;
        this.deck = deck;
        this.currentTurn = new Turn(players.get(0).getID());
        this.gamePhase = GamePhase.SETUP;
    }

    public void startTurn() {
        if (gamePhase != GamePhase.GAMEPLAY) {
            throw new IllegalStateException("A játék nincs játék fázisban.");
        }

        Player currentPlayer = getCurrentPlayer();

        // Új kártya húzása, ha van még a pakliban
        if (deck.hasCards()) {
            TerritoryCard newCard = deck.drawCard();
            currentPlayer.addCard(newCard);
            System.out.println("Játékos új kártyája: " + newCard.getDesign());
        } else {
            System.out.println("A pakli üres, nem lehet több kártyát húzni.");
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
            System.out.println("A játék véget ért!");
        }
    }

    private boolean checkIfGameOver() {
        // Implementáció eldönti, hogy a játék véget ért-ee
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