package ir.doodmood.inkout.services;

import ir.doodmood.inkout.Exception.AlreadyExistsException;
import ir.doodmood.inkout.Exception.NotFoundException;
import ir.doodmood.inkout.Exception.WrongPassException;
import ir.doodmood.inkout.models.Certificate;
import ir.doodmood.inkout.models.Education;
import ir.doodmood.inkout.models.JobPosition;
import ir.doodmood.inkout.models.User;
import ir.doodmood.inkout.models.request.*;
import ir.doodmood.inkout.models.response.UserResponse;
import ir.doodmood.inkout.repositories.UserRepository;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.Service;

@Service
public class UsersService {
    private final UserRepository userRepository;

    @Autowired
    public UsersService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse find(UserFindRequest uf) throws NotFoundException {
        User u = userRepository.getUser(uf.getId());
        if (u == null) throw new NotFoundException();
        return new UserResponse(u);
    }

    public UserResponse register(UserRegisterRequest ur) throws AlreadyExistsException {
        if (userRepository.getUserByEmail(ur.getEmail()) != null)
            throw new AlreadyExistsException("Email already exists");
        return new UserResponse(userRepository.saveUser(new User(ur)));
    }

    public long checkPassword(UserLoginRequest ul) throws NotFoundException, WrongPassException {
        User u = userRepository.getUserByEmail(ul.getEmail());
        if (u == null) throw new NotFoundException();
        if (!User.getPasswordHash(u.getEmail(), ul.getPassword()).equals(u.getPasswordHash())) throw new WrongPassException();
        return u.getId();
    }

    public User addCertificate(NewCertificateRequest ncr, long id) {
        User u = userRepository.getUser(id);
        u.getCertificates().add(new Certificate(ncr, id));
        userRepository.saveUser(u);
        return u;
    }

    public User addEducation(NewEducationRequest ner, long id) {
        User u = userRepository.getUser(id);
        u.getEducation().add(new Education(ner, id));
        userRepository.saveUser(u);
        return u;
    }

    public User addJobPos(NewUserJobPositionRequest nujpr, long id) {
        User u = userRepository.getUser(id);
        u.getJobPositions().add(new JobPosition(nujpr, id));
        userRepository.saveUser(u);
        return u;
    }


}
