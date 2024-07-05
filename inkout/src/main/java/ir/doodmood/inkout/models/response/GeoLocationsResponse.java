package ir.doodmood.inkout.models.response;

import ir.doodmood.inkout.models.GeoLocation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GeoLocationsResponse {
    private List<GeoLocationResponse> locations;

    public GeoLocationsResponse(List<GeoLocation> geos) {
        locations = new ArrayList<>();
        for (GeoLocation geo : geos)
            locations.add(new GeoLocationResponse(geo));
    }
}
