package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class Hero {
    private Boolean alive;
    private String name;
    private Integer health;
    private Integer damage;
}
