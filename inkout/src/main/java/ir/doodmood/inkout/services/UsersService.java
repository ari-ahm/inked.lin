package ir.doodmood.inkout.services;

import ir.doodmood.inkout.Exception.AlreadyExistsException;
import ir.doodmood.inkout.Exception.NotFoundException;
import ir.doodmood.inkout.Exception.WrongPassException;
import ir.doodmood.inkout.models.User;
import ir.doodmood.inkout.models.request.UserFind;
import ir.doodmood.inkout.models.request.UserLogin;
import ir.doodmood.inkout.models.request.UserRegister;
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

    public UserResponse find(UserFind uf) throws NotFoundException {
        User u = userRepository.getUser(uf.getId());
        if (u == null) throw new NotFoundException();
        return new UserResponse(u);
    }

    public UserResponse register(UserRegister ur) throws AlreadyExistsException {
        if (userRepository.getUserByUsername(ur.getUsername()) != null)
            throw new AlreadyExistsException("Username already exists");
        if (userRepository.getUserByEmail(ur.getEmail()) != null)
            throw new AlreadyExistsException("Email already exists");
        return new UserResponse(userRepository.addUser(ur));
    }

    public long checkPassword(UserLogin ul) throws NotFoundException, WrongPassException {
        User u;
        if (ul.getEmail() == null) {
            u = userRepository.getUserByUsername(ul.getUsername());
        } else {
            u = userRepository.getUserByEmail(ul.getEmail());
        }
        if (u == null) throw new NotFoundException();
        if (!User.getPasswordHash(u.getUsername(), ul.getPassword()).equals(u.getPasswordHash())) throw new WrongPassException();
        return u.getId();
    }
}
