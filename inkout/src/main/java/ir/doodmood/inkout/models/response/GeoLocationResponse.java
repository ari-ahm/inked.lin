package ir.doodmood.inkout.models.response;

import ir.doodmood.inkout.models.GeoLocation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeoLocationResponse {
    private long id;
    private String country;
    private String city;

    public GeoLocationResponse(GeoLocation location) {
        this.id = location.getId();
        this.country = location.getCountry();
        this.city = location.getCity();
    }
}
