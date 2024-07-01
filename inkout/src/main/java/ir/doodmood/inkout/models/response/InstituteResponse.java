package ir.doodmood.inkout.models.response;

import ir.doodmood.inkout.models.Institute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstituteResponse {
    private long id;
    private String name;
    private String description;
    private String location;

    public InstituteResponse(Institute institute) {
        this.id = institute.getId();
        this.name = institute.getName();
        this.description = institute.getDescription();
        this.location = institute.getLocation();
    }
}
