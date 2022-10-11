package model;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class Leader {
    private Boolean alive;
    private String name;
    private Integer health;
    private Integer damage;
}
