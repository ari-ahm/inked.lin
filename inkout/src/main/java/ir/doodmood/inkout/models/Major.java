package ir.doodmood.inkout.models;

import ir.doodmood.inkout.models.request.NewMajorRequest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "majors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Major {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String description;

    public Major(NewMajorRequest nmr) {
        this.name = nmr.getName();
        this.description = nmr.getDescription();
    }
}
