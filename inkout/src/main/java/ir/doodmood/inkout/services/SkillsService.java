package ir.doodmood.inkout.services;

import ir.doodmood.inkout.Exception.NotFoundException;
import ir.doodmood.inkout.models.Skill;
import ir.doodmood.inkout.models.request.NewSkillRequest;
import ir.doodmood.inkout.models.response.SkillResponse;
import ir.doodmood.inkout.repositories.SkillsRepository;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.Service;

import java.util.Optional;

@Service
public class SkillsService {
    SkillsRepository skillsRepository;

    @Autowired
    public SkillsService(SkillsRepository skillsRepository) {
        this.skillsRepository = skillsRepository;
    }

    public SkillResponse create(NewSkillRequest newSkillRequest) {
        Skill passion = new Skill(newSkillRequest);
        return new SkillResponse(skillsRepository.save(passion));
    }

    public SkillResponse findById(long id) throws NotFoundException {
        Optional<Skill> oi = skillsRepository.findById(id);
        if (oi.isEmpty())
            throw new NotFoundException();
        return new SkillResponse(oi.get());
    }
}
