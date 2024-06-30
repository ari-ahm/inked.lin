package ir.doodmood.inkout.controllers;

// TODO: implement CRUD and forgetPassword

import ir.doodmood.inkout.Exception.AlreadyExistsException;
import ir.doodmood.inkout.Exception.NotFoundException;
import ir.doodmood.inkout.Exception.WrongPassException;
import ir.doodmood.inkout.models.JwtAuth;
import ir.doodmood.inkout.models.request.UserFindRequest;
import ir.doodmood.inkout.models.request.UserLoginRequest;
import ir.doodmood.inkout.models.request.UserRegisterRequest;
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
    private void get(MashtDTO dto) {
        UserFindRequest request = new UserFindRequest();
        request.setId(Long.parseLong(dto.getPathVariables().get(0)));
        UserResponse ur;
        try {
            ur = usersService.find(request);
        } catch (NotFoundException e) {
            dto.sendResponse(404, "Not Found");
            return;
        }
        dto.sendResponse(ur);
    }

    @PostMapping("/login")
    private void login(MashtDTO dto) {
        UserLoginRequest request = (UserLoginRequest) dto.getRequestBody(UserLoginRequest.class);
        if (request == null || (request.getUsername() == null && request.getEmail() == null)) {
            dto.sendResponse(400, "Bad Request");
            return;
        }

        try {
            long id = usersService.checkPassword(request);
            dto.setJWTToken(new JwtAuth(id));
            dto.sendResponse(200, "ok");
        } catch(NotFoundException e) {
            dto.sendResponse(404, "Not Found");
        } catch(WrongPassException e) {
            dto.sendResponse(401, "Wrong Password");
        }
    }

    @PostMapping
    private void post(MashtDTO dto) {
        UserRegisterRequest request = (UserRegisterRequest) dto.getRequestBody(UserRegisterRequest.class);
        UserResponse ur;
        try {
            ur = usersService.register(request);
        } catch (AlreadyExistsException e) {
            dto.sendResponse(409, e.getMessage());
            return;
        }
        dto.sendResponse(ur);
    }
}
