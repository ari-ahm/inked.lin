package ir.doodmood.inkout.models;

import ir.doodmood.inkout.models.request.NewSkillRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String description;

    public Skill(NewSkillRequest nsr) {
        this.name = nsr.getName();
        this.description = nsr.getDescription();
    }
}
