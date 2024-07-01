package ir.doodmood.inkout.models.response;

import ir.doodmood.inkout.models.Skill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SkillResponse {
    private long id;
    private String name;
    private String description;

    public SkillResponse(Skill skill) {
        this.id = skill.getId();
        this.name = skill.getName();
        this.description = skill.getDescription();
    }
}
