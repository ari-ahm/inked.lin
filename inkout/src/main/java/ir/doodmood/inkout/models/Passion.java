package ir.doodmood.inkout.models;

// TODO search

import ir.doodmood.inkout.models.request.NewPassionRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "passions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id"})
public class Passion {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String description;

    public Passion(NewPassionRequest npr) {
        this.name = npr.getName();
        this.description = npr.getDescription();
    }
}
