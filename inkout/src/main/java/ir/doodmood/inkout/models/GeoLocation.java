package ir.doodmood.inkout.models;

// TODO import locations when starting up...

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "geolocations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id"})
public class GeoLocation {
    @Id
    @GeneratedValue
    private long id;
    private String country;
    private String city;
}
