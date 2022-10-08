package model;

import java.util.Vector;

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
    Vector<Card> hand;
    Deck deck;
    Card field[][];

    public Player(User user) {
        login = user.getLogin();
        hand = new Vector<>();
        deck = new Deck();
        for (int i = 0; i < 5; ++i) {
            hand.add(deck.takeCard());
        }
    }

    //exception MaxCardInHandExc можно описать здесь
    public void takeCardFromDeck() {
        hand.add(deck.takeCard());
    }

    //Также здесь можно описать CardNotFOund
    public Card takeCardFromHand(Integer i) {
        Card card = hand.get(i);
        this.hand.remove(card);
        return card;
    }
}
