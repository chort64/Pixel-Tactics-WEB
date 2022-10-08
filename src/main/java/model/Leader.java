package model;
import lombok.Setter;
import lombok.Getter;

public enum Leader {
    
    WARRIOR(20, 2, true, true),
    ARCHER(20, 2, false, true),
    MAGE(30, 1, false, true);

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

    Leader (Integer damage, Integer health, Boolean melee, Boolean alive) {
        this.damage = damage;
        this.health = health;
        this.melee = melee;
        this.alive = alive;
    }
}
