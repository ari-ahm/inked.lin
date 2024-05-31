package ir.doodmood.inkout.services;

import ir.doodmood.inkout.models.request.UserFind;
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

    public UserResponse find(UserFind uf) {
        return new UserResponse(userRepository.getUser(uf.getId()));
    }

    public UserResponse register(UserRegister ur) {
        return new UserResponse(userRepository.addUser(ur));
    }
}
