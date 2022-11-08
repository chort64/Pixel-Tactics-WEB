package model;
import java.util.ArrayList;
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
        Hero war1 = new Hero(true, "War1", 10, 4, true, false);
        Hero war2 = new Hero(true, "War2", 10, 4, true, false);
        Hero war3 = new Hero(true, "War3", 10, 4, true, false);

        Hero arc1 = new Hero(true, "Arc1", 3, 6, false, false);
        Hero arc2 = new Hero(true, "Arc2", 3, 6, false, false);
        Hero arc3 = new Hero(true, "Arc3", 3, 6, false, false);

        Hero mag1 = new Hero(true, "Mag1", 1, 8, false, false);
        Hero mag2 = new Hero(true, "Mag2", 1, 8, false, false);
        Hero mag3 = new Hero(true, "Mag3", 1, 8, false, false);

        Leader warrior = new Leader(true, "Warrior", 30, 5, false, false);
        Leader archer = new Leader(true, "Archer", 20, 5, false, false);
        Leader mage = new Leader(true, "Mage", 10, 10, false, false);

        cards.add(new Card(war1, warrior));
        cards.add(new Card(war2, warrior));
        cards.add(new Card(war3, warrior));

        cards.add(new Card(arc1, archer));
        cards.add(new Card(arc2, archer));
        cards.add(new Card(arc3, archer));

        cards.add(new Card(mag1, mage));
        cards.add(new Card(mag2, mage));
        cards.add(new Card(mag3, mage));
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
