package model;

import java.util.ArrayList;

import com.example.Pixel.Tactics.exception.CardNotFoundException;
import com.example.Pixel.Tactics.exception.MaxCardsInHandException;
import com.example.Pixel.Tactics.exception.OccupiedPlaceException;

import lombok.Data;

/*
 *   Класс Player - это модель игрока с картами, которая 
 *   инициализируется при каждом начале игры.
 *   При создании объекта данного класса, автоматически создается колода и раздается
 *   пять карт в руку
 */

@Data
public class Player {

    String login;
    ArrayList<Card> hand;
    Card field[][];
    Deck deck;

    public Player(User user) {
        login = user.getLogin();
        hand = new ArrayList<>();
        field = new Card[3][3];
        deck = new Deck();
        for (int i = 0; i < 5; ++i) {
            hand.add(deck.takeCard());
        }
    }

    public void takeCardFromDeck() throws MaxCardsInHandException {
        if (hand.size() == 5) {
            throw new MaxCardsInHandException("Max five cards in hand");
        }
        hand.add(deck.takeCard());
    }
    
    public Card takeCardFromHand(Integer i) throws CardNotFoundException, MaxCardsInHandException {
        if (i < 0 || i > hand.size()) {
            throw new CardNotFoundException("You don't have card with this number");
        } 

        Card card = hand.get(i);
        hand.remove(card);
        return card;
    }

    public boolean isAliveLeader() {
        Card leader = field[1][1];
        if (leader == null) return true;
        return leader.getAlive();
    }

    //Функция для обновления статуса ReadyToMove всех карт на поле
    public void updateCardsStatus() {
        for (Card[] row : field)
            for (Card card : row)
                if (card != null) card.setReadyToMove(true);
    }

    public Card getCardWithCoordinates(Integer x, Integer y) throws CardNotFoundException {
        if (x < 0 || x > 2 || y < 0 || y > 2) {
            throw new CardNotFoundException("You can't take card on this cell");
        }
        return field[x][y];
    }
    
    public void setCardWithCoordinates(Integer x, Integer y, Card card) throws OccupiedPlaceException, CardNotFoundException {
        if (x < 0 || x > 2 || y < 0 || y > 2) {
            throw new CardNotFoundException("You can move on this cell"); //ToDo: мб заменить ошибку
        }
        else if (field[x][y] != null) {
            throw new OccupiedPlaceException("This place if occupied");
        }
        field[x][y] = card;
    }
}