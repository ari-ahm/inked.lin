package ir.doodmood.inkout.controllers;

import ir.doodmood.inkout.models.response.GeoLocationsResponse;
import ir.doodmood.inkout.services.GeoLocationsService;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.http.GetMapping;
import ir.doodmood.mashtframework.annotation.http.RestController;
import ir.doodmood.mashtframework.web.MashtDTO;

@RestController("/locations")
public class GeoLocationsController {
    private GeoLocationsService geoLocationsService;
    private static GeoLocationsResponse geoLocationsResponse;

    @Autowired
    private GeoLocationsController(GeoLocationsService geoLocationsService) {
        this.geoLocationsService = geoLocationsService;
    }

    @GetMapping
    private void get(MashtDTO dto) {
        if (geoLocationsResponse == null) {
            geoLocationsResponse = new GeoLocationsResponse();
            geoLocationsResponse.setLocations(geoLocationsService.getLocations());
        }

        dto.sendResponse(geoLocationsResponse);
    }
}
