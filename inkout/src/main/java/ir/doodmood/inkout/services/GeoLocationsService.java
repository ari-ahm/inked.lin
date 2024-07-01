package ir.doodmood.inkout.services;

import ir.doodmood.inkout.models.GeoLocation;
import ir.doodmood.inkout.repositories.GeoLocationsRepository;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.Service;

import java.util.List;

@Service
public class GeoLocationsService {
    private GeoLocationsRepository geoLocationsRepository;

    @Autowired
    private GeoLocationsService(GeoLocationsRepository geoLocationsRepository) {
        this.geoLocationsRepository = geoLocationsRepository;
    }

    public List<GeoLocation> getLocations() {
        return geoLocationsRepository.findAll();
    }
}
