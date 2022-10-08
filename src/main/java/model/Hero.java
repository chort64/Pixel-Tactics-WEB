package model;

import lombok.Getter;
import lombok.Setter;

public enum Hero { 

    WARRIOR(2, 2, true, true),
    ARCHER(2, 2, false, true),
    MAGE(4, 1, false, true);

    @Getter
    @Setter    
    private Integer damage;

    @Getter
    @Setter    
    private Integer health;

    @Getter
    @Setter    
    private Boolean melee;

    @Getter
    @Setter    
    private Boolean alive;

    Hero (Integer damage, Integer health, Boolean melee, Boolean alive) {
        this.damage = damage;
        this.health = health;
        this.melee = melee;
        this.alive = alive;
    }
}
