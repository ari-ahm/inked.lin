package ir.doodmood.inkout.controllers;

import ir.doodmood.inkout.models.request.UserFind;
import ir.doodmood.inkout.models.request.UserRegister;
import ir.doodmood.inkout.models.response.UserResponse;
import ir.doodmood.inkout.services.UsersService;
import ir.doodmood.mashtframework.annotation.Autowired;
import ir.doodmood.mashtframework.annotation.http.GetMapping;
import ir.doodmood.mashtframework.annotation.http.PostMapping;
import ir.doodmood.mashtframework.annotation.http.RestController;
import ir.doodmood.mashtframework.core.Logger;
import ir.doodmood.mashtframework.web.MashtDTO;

@RestController("/users")
public class Users {
    private final UsersService usersService;
    private final Logger logger;

    @Autowired
    private Users(UsersService usersService, Logger logger) {
        this.usersService = usersService;
        this.logger = logger;
    }

    @GetMapping("/{}")
    private void get(MashtDTO dto) throws Exception {
        UserFind request = new UserFind();
        request.setId(Long.parseLong(dto.getPathVariables().get(0)));
        UserResponse ur = usersService.find(request);
        dto.sendResponse(ur);
    }

    @PostMapping
    private void post(MashtDTO dto) throws Exception {
        UserRegister request = (UserRegister) dto.getRequestBody(UserRegister.class);
        UserResponse ur = usersService.register(request);
        dto.sendResponse(ur);
    }
}
