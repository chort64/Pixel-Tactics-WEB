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
        List<Card> cards = new ArrayList();
        cards.add(new Card(true, "Warrior1", 5, 5));
        cards.add(new Card(true, "Warrior2", 5, 5));
        cards.add(new Card(true, "Warrior3", 5, 5));
        cards.add(new Card(true, "Warrior4", 5, 5));
        cards.add(new Card(true, "Warrior5", 5, 5));
        cards.add(new Card(true, "Mage1", 5, 5));
        cards.add(new Card(true, "Mage2", 5, 5));
        cards.add(new Card(true, "Mage3", 5, 5));
        cards.add(new Card(true, "Mage4", 5, 5));
        cards.add(new Card(true, "Mage5", 5, 5));
        cards.add(new Card(true, "Archer1", 5, 5));
        cards.add(new Card(true, "Archer2", 5, 5));
        cards.add(new Card(true, "Archer3", 5, 5));
        cards.add(new Card(true, "Archer4", 5, 5));
        cards.add(new Card(true, "Archer5", 5, 5));
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
