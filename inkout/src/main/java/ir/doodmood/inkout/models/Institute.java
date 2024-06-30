package ir.doodmood.inkout.models;

import ir.doodmood.inkout.models.request.NewInstituteRequest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "institutes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Institute {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String description;
    private String location;

    public Institute(NewInstituteRequest nir) {
        this.name = nir.getName();
        this.description = nir.getDescription();
        this.location = nir.getLocation();
    }
}
