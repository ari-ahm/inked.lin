package ir.doodmood.inkout.models.response;

import ir.doodmood.inkout.models.GeoLocation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeoLocationsResponse {
    private List<GeoLocation> locations;
}
