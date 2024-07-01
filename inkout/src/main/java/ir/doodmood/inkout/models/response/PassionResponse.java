package ir.doodmood.inkout.models.response;

import ir.doodmood.inkout.models.Passion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PassionResponse {
    private long id;
    private String name;
    private String description;

    public PassionResponse(Passion passion) {
        this.id = passion.getId();
        this.name = passion.getName();
        this.description = passion.getDescription();
    }
}
