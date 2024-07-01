package ir.doodmood.inkout.services;

import ir.doodmood.inkout.Exception.NotFoundException;
import ir.doodmood.inkout.models.Institute;
import ir.doodmood.inkout.models.Passion;
import ir.doodmood.inkout.models.request.NewInstituteRequest;
import ir.doodmood.inkout.models.request.NewPassionRequest;
import ir.doodmood.inkout.models.response.InstituteResponse;
import ir.doodmood.inkout.models.response.PassionResponse;
import ir.doodmood.inkout.repositories.InstitutesRepository;
import ir.doodmood.inkout.repositories.PassionsRepository;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.Service;

import java.util.Optional;

@Service
public class InstitutesService {
    InstitutesRepository institutesRepository;

    @Autowired
    public InstitutesService(InstitutesRepository institutesRepository) {
        this.institutesRepository = institutesRepository;
    }

    public InstituteResponse create(NewInstituteRequest newInstituteRequest) {
        Institute institute = new Institute(newInstituteRequest);
        return new InstituteResponse(institutesRepository.save(institute));
    }

    public InstituteResponse findById(long id) throws NotFoundException {
        Optional<Institute> oi = institutesRepository.findById(id);
        if (oi.isEmpty())
            throw new NotFoundException();
        return new InstituteResponse(oi.get());
    }
}
