package ir.doodmood.inkout.services;

import ir.doodmood.inkout.models.User;
import ir.doodmood.inkout.models.request.ConnectRequest;
import ir.doodmood.inkout.models.request.FollowRequest;
import ir.doodmood.inkout.repositories.UserRepository;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.Service;

@Service
public class ConnectionService {
    private UserRepository userRepository;
    @Autowired
    public ConnectionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void follow(FollowRequest fr, long id) {
        User startUser = userRepository.getUser(id);
        User endUser = userRepository.getUser(fr.getId());
        startUser.getFollowing().add(endUser);
    }

    public void connect(ConnectRequest fr, long id) {
        User startUser = userRepository.getUser(id);
        User endUser = userRepository.getUser(fr.getId());
        if (endUser.getConnections().contains(startUser)) return;
        if (endUser.getOutGoingConnectionRequests().contains(startUser)) {
            endUser.getOutGoingConnectionRequests().remove(startUser);
            endUser.getConnections().add(startUser);
            startUser.getConnections().add(endUser);
            return;
        }
        startUser.getOutGoingConnectionRequests().add(endUser);
    }
}
