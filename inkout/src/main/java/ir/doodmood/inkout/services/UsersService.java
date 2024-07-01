package ir.doodmood.inkout.services;

import ir.doodmood.inkout.Exception.AlreadyExistsException;
import ir.doodmood.inkout.Exception.NotFoundException;
import ir.doodmood.inkout.Exception.WrongPassException;
import ir.doodmood.inkout.models.*;
import ir.doodmood.inkout.models.request.*;
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

    public User find(UserFindRequest uf) throws NotFoundException {
        User u = userRepository.getUser(uf.getId());
        if (u == null) throw new NotFoundException();
        return u;
    }

    public User register(UserRegisterRequest ur) throws AlreadyExistsException {
        if (userRepository.getUserByEmail(ur.getEmail()) != null)
            throw new AlreadyExistsException("Email already exists");
        return userRepository.saveUser(new User(ur));
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

    public User removeCertificate(RemoveCertificateRequest rcr, long id) {
        User u = userRepository.getUser(id);
        for (var i : u.getCertificates()) {
            if (i.getId() == rcr.getId()) {
                u.getCertificates().remove(i);
                break;
            }
        }
        userRepository.saveUser(u);
        return u;
    }

    public User addEducation(NewEducationRequest ner, long id) {
        User u = userRepository.getUser(id);
        u.getEducation().add(new Education(ner, id));
        userRepository.saveUser(u);
        return u;
    }

    public User removeEducation(RemoveEducationRequest rer, long id) {
        User u = userRepository.getUser(id);
        for (var i : u.getEducation()) {
            if (i.getId() == rer.getId()) {
                u.getEducation().remove(i);
                break;
            }
        }
        userRepository.saveUser(u);
        return u;
    }

    public User addJobPos(NewUserJobPositionRequest nujpr, long id) {
        User u = userRepository.getUser(id);
        u.getJobPositions().add(new JobPosition(nujpr, id));
        userRepository.saveUser(u);
        return u;
    }

    public User removeJobPos(RemoveJobPositionRequest rjpr, long id) {
        User u = userRepository.getUser(id);
        for (var i : u.getJobPositions()) {
            if (i.getId() == rjpr.getId()) {
                u.getJobPositions().remove(i);
                break;
            }
        }
        userRepository.saveUser(u);
        return u;
    }

    public User addSkill(AddSkill2UserRequest as2ur, long id) {
        User u = userRepository.getUser(id);
        u.getSkills().add(Skill.builder().id(as2ur.getSkill()).build());
        userRepository.saveUser(u);
        return u;
    }

    public User removeSkill(RemoveSkillRequest rsr, long id) {
        User u = userRepository.getUser(id);
        for (var i : u.getSkills()) {
            if (i.getId() == rsr.getId()) {
                u.getSkills().remove(i);
                break;
            }
        }
        userRepository.saveUser(u);
        return u;
    }

    public User setContactInfo(SetContactInfoRequest scir, long id) {
        User u = userRepository.getUser(id);
        u.setContact(new ContactInfo(scir, u));
        userRepository.saveUser(u);
        return u;
    }

    public User update(UserRegisterRequest ur, long id) { // TODO handle email change
        User u = new User(ur);
        u.setId(id);
        userRepository.saveUser(u);
        return u;
    }

    public void removeUser(long id) {
        userRepository.removeUserById(id);
    }
}
