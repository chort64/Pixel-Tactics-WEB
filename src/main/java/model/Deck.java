package model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import lombok.Data;

/*  
 *  Колода создается при каждой игре. 
 *  Соответственно берём все карты из enum Card и перемешиваем.
 *  Сама колода представляет собой стек.
 *  Отдельно реализовал метод взятие карты.
 */
@Data
public class Deck {
    private Stack<Card> deck = new Stack();

    public Deck() {
        //Test
        Card war = new Card(true, "Warrior", 5, 5);
        Card mag = new Card(true, "Mage", 5, 5);
        Card arc = new Card(true, "Archer", 5, 5);
        List<Card> cards = new ArrayList();
        cards.add(war);
        cards.add(war);
        cards.add(war);
        cards.add(war);
        cards.add(war);
        cards.add(mag);
        cards.add(mag);
        cards.add(mag);
        cards.add(mag);
        cards.add(mag);
        cards.add(arc);
        cards.add(arc);
        cards.add(arc);
        cards.add(arc);
        cards.add(arc);
        //

        Collections.shuffle(cards);
        deck.addAll(cards);       
    }

    public Card takeCard() {
        Card peekCard = deck.peek();
        deck.pop();
        return peekCard;
    }
}
