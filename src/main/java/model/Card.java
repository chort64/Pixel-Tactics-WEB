package model;

import lombok.Data;

/*
 *  Card - перечислитесь названий карт.
 *  Здесь буду собраны все названий карт. Каждое название будет также отсылать
 *  к классу Hero или Leader в зависимости от ситуации.
 */

@Data
public class Card {

    private Hero hero;
    private Leader leader;

    private Boolean alive;
    private String name;
    private Integer health;
    private Integer damage;
    private Boolean readyToMove;

    public Card(Hero hero, Leader leader) {
        this.hero = hero;
        this.leader = leader;
    }

    public void newHero() {
        alive = true;
        readyToMove = true;
        this.name = this.hero.getName();
        this.health = this.hero.getHealth();
        this.damage = this.hero.getDamage();
    }

    public void newLeader() {
        alive = true;
        readyToMove = true;
        this.name = this.leader.getName();
        this.health = this.leader.getHealth();
        this.damage = this.leader.getDamage();
    }
}
