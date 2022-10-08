package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/*
 *  Card - перечислитель названий карт.
 *  Здесь буду собраны все названий карт. Каждое название будет также отсылать
 *  к классу Hero или Leader в зависимости от ситуации.
 */

//Продумать логику того, как у меня соответствуют карты в плане герой <-> лидер.
@Data
@AllArgsConstructor
public class Card {
    private Boolean alive;
    private String name;
    private Integer health;
    private Integer damage;
}


// public enum Card {

//     //просто наполин пока колоду тремя картами
//     WARRIOR1(Hero.WARRIOR, Leader.WARRIOR),
//     WARRIOR2(Hero.WARRIOR, Leader.WARRIOR), 
//     WARRIOR3(Hero.WARRIOR, Leader.WARRIOR), 
//     WARRIOR4(Hero.WARRIOR, Leader.WARRIOR), 
//     WARRIOR5(Hero.WARRIOR, Leader.WARRIOR),  
//     MAGE1(Hero.MAGE, Leader.MAGE),
//     MAGE2(Hero.MAGE, Leader.MAGE),
//     MAGE3(Hero.MAGE, Leader.MAGE),
//     MAGE4(Hero.MAGE, Leader.MAGE),
//     MAGE5(Hero.MAGE, Leader.MAGE),
//     ARCHER1(Hero.ARCHER, Leader.ARCHER),
//     ARCHER2(Hero.ARCHER, Leader.ARCHER),
//     ARCHER3(Hero.ARCHER, Leader.ARCHER),
//     ARCHER4(Hero.ARCHER, Leader.ARCHER),
//     ARCHER5(Hero.ARCHER, Leader.ARCHER);
    
//     @Getter
//     @Setter
//     private Hero hero;

//     @Getter
//     @Setter
//     private Leader leader;

//     Card (Hero hero, Leader leader) {
//         this.hero = hero;
//         this.leader = leader;
//     }
// }
