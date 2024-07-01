package ir.doodmood.inkout.services;

import ir.doodmood.inkout.Exception.NotFoundException;
import ir.doodmood.inkout.models.Passion;
import ir.doodmood.inkout.models.request.NewPassionRequest;
import ir.doodmood.inkout.models.response.PassionResponse;
import ir.doodmood.inkout.repositories.PassionsRepository;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.Service;

import java.util.Optional;

@Service
public class PassionsService {
    PassionsRepository passionsRepository;

    @Autowired
    public PassionsService(PassionsRepository passionsRepository) {
        this.passionsRepository = passionsRepository;
    }

    public PassionResponse create(NewPassionRequest newPassionRequest) {
        Passion passion = new Passion(newPassionRequest);
        return new PassionResponse(passionsRepository.save(passion));
    }

    public PassionResponse findById(long id) throws NotFoundException {
        Optional<Passion> op = passionsRepository.findById(id);
        if (op.isEmpty())
            throw new NotFoundException();
        return new PassionResponse(op.get());
    }
}
