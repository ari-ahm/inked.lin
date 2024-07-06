package ir.doodmood.inkout.services;

import ir.doodmood.inkout.Exception.AlreadyExistsException;
import ir.doodmood.inkout.Exception.NotFoundException;
import ir.doodmood.inkout.Exception.WrongPassException;
import ir.doodmood.inkout.models.*;
import ir.doodmood.inkout.models.request.*;
import ir.doodmood.inkout.models.response.UserResponse;
import ir.doodmood.inkout.repositories.ProxiesRepository;
import ir.doodmood.inkout.repositories.UserRepository;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class UsersService {
    private final UserRepository userRepository;
    private final ProxiesRepository proxiesRepository;

    @Autowired
    public UsersService(UserRepository userRepository, ProxiesRepository proxiesRepository) {
        this.userRepository = userRepository;
        this.proxiesRepository = proxiesRepository;
    }

    public User find(UserFindRequest uf) throws NotFoundException {
        User u = userRepository.getUser(uf.getId());
        if (u == null) throw new NotFoundException();
        return u;
    }

    public User register(UserRegisterRequest ur) throws AlreadyExistsException {
        if (userRepository.getUserByEmail(ur.getEmail()) != null)
            throw new AlreadyExistsException("Email already exists");
        return userRepository.saveUser(new User(ur, proxiesRepository));
    }

    public long checkPassword(UserLoginRequest ul) throws NotFoundException, WrongPassException {
        User u = userRepository.getUserByEmail(ul.getEmail());
        if (u == null) throw new NotFoundException();
        if (!User.getPasswordHash(u.getEmail(), ul.getPassword()).equals(u.getPasswordHash())) throw new WrongPassException();
        return u.getId();
    }

    public User addCertificate(NewCertificateRequest ncr, long id) {
        User u = userRepository.getUser(id);
        u.getCertificates().add(proxiesRepository.save(new Certificate(ncr, u, proxiesRepository)));
        userRepository.updateUser(u);
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
        userRepository.updateUser(u);
        return u;
    }

    public User addEducation(NewEducationRequest ner, long id) {
        User u = userRepository.getUser(id);
        u.getEducation().add(proxiesRepository.save(new Education(ner, u, proxiesRepository)));
        userRepository.updateUser(u);
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
        userRepository.updateUser(u);
        return u;
    }

    public User addJobPos(NewUserJobPositionRequest nujpr, long id) {
        User u = userRepository.getUser(id);
        u.getJobPositions().add(proxiesRepository.save(new JobPosition(nujpr, u, proxiesRepository)));
        userRepository.updateUser(u);
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
        userRepository.updateUser(u);
        return u;
    }

    public User addSkill(AddSkill2UserRequest as2ur, long id) {
        User u = userRepository.getUser(id);
        u.getSkills().add(proxiesRepository.getProxy(Skill.class, as2ur.getSkill()));
        userRepository.updateUser(u);
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
        userRepository.updateUser(u);
        return u;
    }

    public User setContactInfo(SetContactInfoRequest scir, long id) {
        User u = userRepository.getUser(id);
        u.setContact(proxiesRepository.save(new ContactInfo(scir, u)));
        userRepository.updateUser(u);
        return u;
    }

    public User update(UserRegisterRequest ur, long id) { // TODO handle email change
        User u = new User(ur, proxiesRepository);
        u.setId(id);
        userRepository.updateUser(u);
        return u;
    }

    public void removeUser(long id) {
        userRepository.removeUserById(id);
    }

    public void post(NewPostRequest npr, long id) {
        User u = userRepository.getUser(id);
        u.getPosts().add(proxiesRepository.save(new Post(npr, u)));
        userRepository.updateUser(u);
    }

    public void comment(NewCommentRequest npr, long id) {
        proxiesRepository.save(new Comment(npr, proxiesRepository.getProxy(User.class, id), proxiesRepository));
    }

    public List<UserResponse> search(UserSearchRequest scir, long id) {
        LinkedList<UserResponse> ret = new LinkedList<>();
        for (User i : userRepository.search(scir.getSearchText(), id)) {
            ret.add(new UserResponse(i));
        }
        return ret;
    }
}
