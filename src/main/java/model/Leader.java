package model;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Leader {
    private Boolean alive;
    private String name;
    private Integer health;
    private Integer damage;
    private Boolean isMelee;
    private Boolean interception;
}
